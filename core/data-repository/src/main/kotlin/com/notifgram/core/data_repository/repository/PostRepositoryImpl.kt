package com.notifgram.core.data_repository.repository

import android.content.res.Resources.NotFoundException
import androidx.annotation.Keep
import com.notifgram.core.common.MyLog.i
import com.notifgram.core.data_repository.data_source.file.FileDataSource
import com.notifgram.core.data_repository.data_source.local.LocalPostDataSource
import com.notifgram.core.data_repository.data_source.remote.RemotePostDataSource
import com.notifgram.core.domain.entity.Post
import com.notifgram.core.domain.entity.settings.AppSettings
import com.notifgram.core.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import javax.inject.Singleton

@Keep
@Singleton
class PostRepositoryImpl @Inject constructor(
    private val remotePostDataSource: RemotePostDataSource,
    private val localPostDataSource: LocalPostDataSource,
    private val fileDataSource: FileDataSource,
) : PostRepository {

    @Inject
    lateinit var appSettings: AppSettings

    override suspend fun getPost(id: Int): Post =
        localPostDataSource.getPost(id) ?: throw NotFoundException()

    override fun getPostFlow(id: Int): Flow<Post?> =
        localPostDataSource.getPostFlow(id)

    override suspend fun getPostsLocally(): List<Post> =
        localPostDataSource.getAll()

    override suspend fun getPostsLocally(searchText: String): List<Post> =
        localPostDataSource.searchByName(searchText)

    override fun getPostsFlowAndManageDecryption(): Flow<List<Post>> {
        val shouldEncrypt: Boolean = appSettings.shouldEncrypt
        i("$TAG getPostsFlowAndManageDecryption shouldEncrypt=$shouldEncrypt")

        if (shouldEncrypt) {
            return localPostDataSource.getAllFlow()
                .map { list ->
                    list.map { post ->
                        post.copy(
                            mediaFile = fileDataSource.openFileDecrypted(post.mediaFile!!)?.let {
                                fileDataSource.createTempFile(
                                    fileName = post.mediaFile!!.name,
                                    it
                                )
                            }
                        )
                    }
                }.onEach {
                    i("$TAG getPostsFlowAndManageDecryption count=${it.count()}")
            }
        } else
            return getPostsLocallyFlow()

    }

    // used
//    override fun getPostsFlowWithMediaContent(): Flow<List<Post>> {
//        i("$TAG getPostsFlowWithMediaContent()")
//        val allPosts = localPostDataSource.getAllFlow()
//            .map { list ->
//                list.map { post ->
//                    post.copy(
//                        mediaContent = fileDataSource.readFile(post.mediaFile!!)
//                    )
//                }
//            }.onEach {
//                i("$TAG getPostsFlowWithMediaContent count=${it.count()}")
//            }
//        return allPosts
//    }


    override fun getPostsLocallyFlow(): Flow<List<Post>> {
        i("$TAG getPostsLocallyFlow()")
        return localPostDataSource.getAllFlow()
    }

    override suspend fun deletePostLocally(id: Int) {
        i("$TAG deletePostLocally()")
        localPostDataSource.delete(id)
    }

    override suspend fun deleteAllPostsLocally() {
        i("$TAG deleteAllPostsLocally()")
        localPostDataSource.clearPostListings()
    }

    suspend fun insertPostLocally(post: Post) {
        i("$TAG insertPostLocally()")
        post.let { item ->
            localPostDataSource.insertPost(item)
        }
    }

    override suspend fun upsertPostLocally(post: Post) {
        i("$TAG upsertPostLocally()")
        post.let { item ->
            localPostDataSource.upsertPost(item)
        }
    }

    /**
     * Used for generating samples
     */
    override suspend fun insertPostsLocally(items: List<Post>) {
        i("$TAG insertPostsLocally() items=$items")
        items.let { listings ->
            localPostDataSource.insertPosts(listings)
        }
    }

    /////////////////////////////////////////////////////////////
    /////////////////////      REMOTE     ///////////////////////
    /////////////////////////////////////////////////////////////
    override suspend fun getPostsFromRemote(): Flow<List<Post>> {
        i("$TAG getPostsFromRemote()")
        return remotePostDataSource.getPosts()
    }

    override suspend fun insertPostRemotely(item: Post) {
        i("$TAG insertPostRemotely() item=$item")
        val x = remotePostDataSource.postPost(item)
    }

    companion object {
        private const val TAG = "PostRepositoryImpl"
    }

}