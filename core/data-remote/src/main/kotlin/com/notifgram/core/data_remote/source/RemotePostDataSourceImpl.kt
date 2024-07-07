package com.notifgram.core.data_remote.source

import com.notifgram.core.common.MyLog.i
import com.notifgram.core.data_remote.networking.post.PostApi
import com.notifgram.core.data_remote.networking.post.PostDto
import com.notifgram.core.data_remote.networking.post.toPost
import com.notifgram.core.data_remote.networking.post.toPostDto
import com.notifgram.core.data_repository.FileUriBuilder
import com.notifgram.core.data_repository.data_source.remote.RemotePostDataSource
import com.notifgram.core.data_repository.sync.LastRemoteChange
import com.notifgram.core.domain.entity.Post
import com.notifgram.core.domain.entity.UseCaseException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class RemotePostDataSourceImpl @Inject constructor(
    private val postApi: PostApi,
    val fileUriBuilder: FileUriBuilder
) : RemotePostDataSource {

    override suspend fun updatePost(item: Post): Flow<Post> {
        i("$TAG updatePost item=$item")
        val post = postApi.update(item.id, item.toPostDto()).body()
        i("$TAG updatePost returned result: post=$post")
        return flow<Post> {

            if (post != null)
                emit(post.toPost(fileUriBuilder))
            else
                throw UseCaseException.PostException(Throwable("Response does not contain returned post"))
        }.catch {
            throw UseCaseException.PostException(it)
        }
    }

    override suspend fun getPostChangeList(after: String): List<LastRemoteChange> {
        i("$TAG getPostChangeList() after=$after")
        val changedPostsIds = postApi.getPostChangeList(after = after)
        i("$TAG getPostChangeList() changedPostsIds=$changedPostsIds")
        return changedPostsIds
    }

    override suspend fun deleteAllPosts() {
        i("$TAG deleteAllPosts")
        postApi.deleteAll()
    }

    override suspend fun deletePost(id: Int): Flow<Boolean> {
        i("$TAG deletePost id=$id")
        val isSuccess: Boolean = postApi.delete(id).body()
            ?: throw UseCaseException.PostException(Throwable("response is null"))
        return flow { emit(isSuccess) }
    }

    override fun getPosts(): Flow<List<Post>> = flow {
        emit(postApi.getPosts())
    }.map { post ->
        post.map {
            it.toPost(fileUriBuilder)
        }
    }.catch {
        throw UseCaseException.PostException(it)
    }

    override suspend fun getPosts(ids: List<Int>): List<Post> {
        i("$TAG getPosts ids=$ids")
        return postApi.getPosts(ids).map { postDto ->
            postDto.toPost(fileUriBuilder)
        }
    }


    override fun getPost(id: Int): Flow<Post> = flow {
        emit(postApi.getPost(id))
    }.map {
        it.toPost(fileUriBuilder)
    }.catch {
        throw UseCaseException.PostException(it)
    }

    override fun postPost(item: Post) {
        i("$TAG postPost item=$item")

        postApi.postPost(item.toPostDto())?.enqueue(
            object : Callback<PostDto> {
                override fun onFailure(call: Call<PostDto>, t: Throwable) {
                    i("$TAG onFailure t=$t")
                    throw UseCaseException.PostException(t)
                }

                override fun onResponse(
                    call: Call<PostDto>,
                    response: Response<PostDto>
                ) {
                    val post = response.body()?.toPost(fileUriBuilder)
                    i("$TAG onResponse response.body()=${response.body()}")
                    i("$TAG onResponse body=$post")

                }
            }
        )
//        return flow { emit(post ) }
    }


    companion object {
        private const val TAG = "RemotePostDataSourceImpl"
    }

}

