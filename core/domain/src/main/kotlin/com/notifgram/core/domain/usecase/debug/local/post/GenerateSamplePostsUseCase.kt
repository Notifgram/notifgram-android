package com.notifgram.core.domain.usecase.debug.local.post

import com.notifgram.core.domain.entity.Post
import com.notifgram.core.domain.repository.PostRepository
import javax.inject.Inject

class GenerateSamplePostsUseCase @Inject constructor(
    private val postRepository: PostRepository
) {

    suspend fun generateSamplePostsLocally(posts: List<Post>) {
        postRepository.insertPostsLocally(posts)
    }

}