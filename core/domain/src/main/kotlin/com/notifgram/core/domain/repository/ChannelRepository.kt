package com.notifgram.core.domain.repository

import com.notifgram.core.domain.entity.Channel
import kotlinx.coroutines.flow.Flow

interface ChannelRepository {

    suspend fun getChannelsLocally(searchText: String): List<Channel>
    suspend fun getChannelsLocally(): List<Channel>
    fun getChannelsLocallyFlow(): Flow<List<Channel>>
//    suspend fun getChannel(id: Int): Channel?
    fun getChannelFlow(id: Int): Flow<Channel?>

    suspend fun insertChannelsLocally(items: List<Channel>)
    suspend fun upsertChannelLocally(channel: Channel)
    suspend fun deleteChannelLocally(id: Int)

    suspend fun deleteAllChannelsLocally()

    suspend fun toggleFollowingChannel(id: Int)

    fun getAllFollowedChannelsIdsFlow(): Flow<List<Int>>

    /////////////////////////////////////////////////////////////
    /////////////////////      REMOTE     ///////////////////////
    /////////////////////////////////////////////////////////////
    suspend fun getChannelsFromRemote(): Flow<List<Channel>>

    suspend fun insertChannelRemotely(item: Channel)
}