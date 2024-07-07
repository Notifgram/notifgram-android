package com.notifgram.core.data_repository.data_source.local

import com.notifgram.core.domain.entity.Channel
import kotlinx.coroutines.flow.Flow

interface LocalChannelDataSource {
    suspend fun searchByName(searchText: String): List<Channel>
    fun searchByNameFlow(searchText: String): Flow<List<Channel>>
    fun getAllFlow(): Flow<List<Channel>>
    suspend fun getAll(): List<Channel>
    fun getChannels(idsSet: Set<Int>): Flow<List<Channel>>
    suspend fun getChannel(id: Int): Channel?
    fun getChannelFlow(id: Int): Flow<Channel?>
    fun getAllFollowedChannelsIdsFlow(): Flow<List<Int>>

    suspend fun updateChannel(channel: Channel)
    suspend fun insertChannels(items: List<Channel>)
    suspend fun insertChannel(item: Channel)
    suspend fun upsertChannel(item: Channel)
    suspend fun upsertChannels(channels: List<Channel>)

    suspend fun clearChannelListings()
    suspend fun delete(item: Channel): Int
    suspend fun delete(id: Int): Int

    /**
     * Deletes rows in the db matching the specified [ids]
     */
    suspend fun deleteChannels(ids: List<Int>): Int

    suspend fun toggleFollowingChannel(id: Int)
}