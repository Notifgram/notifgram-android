package com.notifgram.core.domain.usecase.post

import com.notifgram.core.domain.repository.PostRepository
import javax.inject.Inject

class DeleteAllLocalPostsUseCase @Inject constructor(
    private val postRepository: PostRepository
) {

    suspend fun invoke() {
        postRepository.deleteAllPostsLocally()
    }

}