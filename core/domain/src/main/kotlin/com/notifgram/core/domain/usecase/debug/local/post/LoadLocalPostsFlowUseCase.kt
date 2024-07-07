package com.notifgram.core.domain.usecase.debug.local.post

import com.notifgram.core.domain.entity.Post
import com.notifgram.core.domain.repository.PostRepository
import com.notifgram.core.domain.usecase.UseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LoadLocalPostsFlowUseCase @Inject constructor(
    configuration: Configuration,
    private val postRepository: PostRepository,
) : UseCase<LoadLocalPostsFlowUseCase.Request, LoadLocalPostsFlowUseCase.Response>(
    configuration
) {
    override fun process(request: Request): Flow<Response> =
        postRepository.getPostsLocallyFlow()
            .map {
                Response(it)
            }

    object Request : UseCase.Request
    data class Response(val posts: List<Post>) : UseCase.Response

}