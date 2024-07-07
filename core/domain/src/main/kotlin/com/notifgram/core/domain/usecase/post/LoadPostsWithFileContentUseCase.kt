//package com.notifgram.core.domain.usecase.post
//
////import com.notifgram.core.common.MyLog.i
//import com.notifgram.core.domain.entity.Post
//import com.notifgram.core.domain.repository.PostRepository
//import com.notifgram.core.domain.usecase.UseCase
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.map
//import javax.inject.Inject
//
//class  LoadPostsWithFileContentUseCase @Inject constructor(
//    configuration: Configuration,
//    private val postRepository: PostRepository,
//) : UseCase<LoadPostsWithFileContentUseCase.Request, LoadPostsWithFileContentUseCase.Response>(
//    configuration
//) {
//    override fun process(request: Request): Flow<Response> {
////        i(TAG)
//        return postRepository.getPostsFlowWithMediaContent()
//            .map {
//                Response(it)
//            }
//    }
//
////    override fun process(request: Request): Flow<Response> {
////        i(TAG)
////        val a= postRepository.getPostsLocallyFlow().map {
////            it.onEach {  }
////        }
//////            .map {
//////                Response(it)
//////            }
////    }
//
//    object Request : UseCase.Request
//    data class Response(val posts: List<Post>) : UseCase.Response
//
//    companion object {
//        private const val TAG = "LoadPostsWithFileContentUseCase"
//    }
//}