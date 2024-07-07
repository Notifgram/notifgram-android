package com.notifgram.core.domain.usecase.debug.remote.channel

import com.notifgram.core.domain.entity.Channel
import com.notifgram.core.domain.repository.ChannelRepository
import javax.inject.Inject

class PostChannelUseCase @Inject constructor(
    private val channelRepository: ChannelRepository
) {

    suspend fun postChannel(item: Channel) {
        println("PostChannelUseCase.postChannel item=$item")

        channelRepository.insertChannelRemotely(item)
    }

}