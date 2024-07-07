package com.notifgram.core.data_repository.data_source.remote

import com.notifgram.core.data_repository.sync.LastRemoteChange
import com.notifgram.core.domain.entity.Channel
import kotlinx.coroutines.flow.Flow

interface RemoteChannelDataSource {

    suspend fun deleteAllChannels()
    suspend fun deleteChannel(id: Int): Flow<Boolean>
    fun getChannels(): Flow<List<Channel>>
    suspend fun getChannels(ids: List<Int>): List<Channel>
    fun getChannel(id: Int): Flow<Channel>
    fun postChannel(item: Channel)
    suspend fun updateChannel(item: Channel): Flow<Channel>

    // Sync
    suspend fun getChannelChangeList(after: String): List<LastRemoteChange>
}
