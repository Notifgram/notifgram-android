package com.notifgram.core.domain.usecase.debug.remote.channel

import com.notifgram.core.domain.entity.Channel
import com.notifgram.core.domain.repository.ChannelRepository
import com.notifgram.core.domain.usecase.UseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetChannelsUseCase @Inject constructor(
    private val channelRepository: ChannelRepository,
) {
    fun process(): Flow<Response> {
        return flow {
            println("$TAG.process")

            val remoteListings: Flow<List<Channel>> =
                channelRepository.getChannelsFromRemote()

            emit(Response(remoteListings))

        }
    }

    object Request : UseCase.Request
    data class Response(val channels: Flow<List<Channel>>) : UseCase.Response

    companion object {
        private const val TAG = "GetChannelsUseCase"
    }

}