package com.notifgram.core.domain.usecase.debug.local.channel

import com.notifgram.core.domain.entity.Channel
import com.notifgram.core.domain.repository.ChannelRepository
import javax.inject.Inject

class GenerateSampleChannelsUseCase @Inject constructor(
    private val channelRepository: ChannelRepository
) {

    suspend fun generateSampleChannelsLocally(channels: List<Channel>) {
        channelRepository.insertChannelsLocally(channels)
    }

}