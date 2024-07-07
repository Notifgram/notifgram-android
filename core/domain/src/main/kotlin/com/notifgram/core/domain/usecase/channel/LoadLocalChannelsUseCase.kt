package com.notifgram.core.domain.usecase.channel

import com.notifgram.core.domain.entity.Channel
import com.notifgram.core.domain.repository.ChannelRepository
import com.notifgram.core.domain.usecase.UseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LoadLocalChannelsUseCase @Inject constructor(
    configuration: Configuration,
    private val channelRepository: ChannelRepository,
) : UseCase<LoadLocalChannelsUseCase.Request, LoadLocalChannelsUseCase.Response>(
    configuration
) {
    override fun process(request: Request): Flow<Response> =
        channelRepository.getChannelsLocallyFlow()
            .map {
                Response(it)
            }

    object Request : UseCase.Request
    data class Response(val channels: List<Channel>) : UseCase.Response

    companion object {
        private const val TAG = "LoadLocalChannelsUseCase"
    }
}