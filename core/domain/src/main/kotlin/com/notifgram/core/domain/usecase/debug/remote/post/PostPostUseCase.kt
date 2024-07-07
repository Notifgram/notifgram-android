package com.notifgram.core.domain.usecase.debug.remote.post

import com.notifgram.core.domain.entity.Post
import com.notifgram.core.domain.repository.PostRepository
import javax.inject.Inject

class PostPostUseCase @Inject constructor(
    private val postRepository: PostRepository
) {

    suspend fun postPost(item: Post) {
        println("PostPostUseCase.postPost item=$item")

        postRepository.insertPostRemotely(item)
    }

}