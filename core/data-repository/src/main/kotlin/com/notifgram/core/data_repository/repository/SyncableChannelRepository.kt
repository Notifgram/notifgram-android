package com.notifgram.core.data_repository.repository

import com.notifgram.core.data_repository.sync.Syncable
import com.notifgram.core.domain.entity.Channel
import kotlinx.coroutines.flow.Flow

interface SyncableChannelRepository : Syncable {

    fun getChannels(): Flow<List<Channel>>

}