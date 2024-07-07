package com.notifgram.core.domain.usecase.channel

import com.notifgram.core.common.FirebaseCloudMessaging
import com.notifgram.core.common.MyLog.i
import com.notifgram.core.domain.repository.ChannelRepository
import com.notifgram.core.domain.usecase.UseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ToggleFollowingChannelUseCase @Inject constructor(
    configuration: Configuration,
    private val channelRepository: ChannelRepository,
) : UseCase<ToggleFollowingChannelUseCase.Request, ToggleFollowingChannelUseCase.Response>(
    configuration
) {
    override fun process(request: Request): Flow<Response> {
        i("$TAG request.id=${request.id}")

        if (request.newValue)
            FirebaseCloudMessaging().subscribeToTopic(request.id.toString())
        else
            FirebaseCloudMessaging().unsubscribeFromTopic(request.id.toString())

        return suspend {
            channelRepository.toggleFollowingChannel(request.id)
        }.asFlow()
            .map {
                ToggleFollowingChannelUseCase.Response
            }

//            .map {
//                Response(it)
//            }
//                .map {
//                    Response
//                }
//        val channel = channelRepository.getChannelFlow(request.id)
//        channelRepository.
//            .map {
//                Response(it)
//            }
//        }
    }

    data class Request(val id: Int, val newValue: Boolean) : UseCase.Request
    object Response : UseCase.Response

    companion object {
        private const val TAG = "ToggleFollowingChannelUseCase"
    }

}