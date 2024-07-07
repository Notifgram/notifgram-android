package com.notifgram.core.domain.usecase.channel

import com.notifgram.core.domain.repository.ChannelRepository
import javax.inject.Inject

class DeleteAllLocalChannelsUseCase @Inject constructor(
    private val channelRepository: ChannelRepository
) {

    suspend fun invoke() {
        channelRepository.deleteAllChannelsLocally()
    }

}