package com.notifgram.core.domain.usecase.debug.remote.post

import com.notifgram.core.domain.entity.Post
import com.notifgram.core.domain.repository.PostRepository
import com.notifgram.core.domain.usecase.UseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetPostsUseCase @Inject constructor(
    private val postRepository: PostRepository,
) {
    fun process(): Flow<Response> {
        return flow {
            println("$TAG.process")

            val remoteListings: Flow<List<Post>> =
                postRepository.getPostsFromRemote()

            emit(Response(remoteListings))

        }
    }

    object Request : UseCase.Request
    data class Response(val posts: Flow<List<Post>>) : UseCase.Response

    companion object {
        private const val TAG = "GetPostsUseCase"
    }

}