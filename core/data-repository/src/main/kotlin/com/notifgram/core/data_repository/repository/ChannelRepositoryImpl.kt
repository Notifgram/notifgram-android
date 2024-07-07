package com.notifgram.core.data_repository.repository

import androidx.annotation.Keep
import com.notifgram.core.common.MyLog.i
import com.notifgram.core.data_repository.data_source.local.LocalChannelDataSource
import com.notifgram.core.data_repository.data_source.remote.RemoteChannelDataSource
import com.notifgram.core.domain.entity.Channel
import com.notifgram.core.domain.repository.ChannelRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Keep
@Singleton
class ChannelRepositoryImpl @Inject constructor(
    private val remoteChannelDataSource: RemoteChannelDataSource,
    private val localChannelDataSource: LocalChannelDataSource,
) : ChannelRepository {

//    override suspend fun getChannel(id: Int): Channel =
//        localChannelDataSource.getChannel(id) ?: throw NotFoundException()

    override fun getChannelFlow(id: Int): Flow<Channel?> =
        localChannelDataSource.getChannelFlow(id)

    override suspend fun getChannelsLocally(): List<Channel> =
        localChannelDataSource.getAll()

    override suspend fun getChannelsLocally(searchText: String): List<Channel> =
        localChannelDataSource.searchByName(searchText)

    override fun getChannelsLocallyFlow(): Flow<List<Channel>> =
        localChannelDataSource.getAllFlow()

    override fun getAllFollowedChannelsIdsFlow(): Flow<List<Int>> =
        localChannelDataSource.getAllFollowedChannelsIdsFlow()

    override suspend fun deleteChannelLocally(id: Int) {
        i("$TAG deleteChannelLocally()")
        localChannelDataSource.delete(id)
    }

    override suspend fun deleteAllChannelsLocally() {
        i("$TAG deleteAllChannelsLocally()")
        localChannelDataSource.clearChannelListings()
    }

    suspend fun updateChannel(item: Channel) {
        i("$TAG updateChannel()")
        localChannelDataSource.updateChannel(item)
    }

    suspend fun insertChannelLocally(channel: Channel) {
        i("$TAG insertChannelLocally()")
        channel.let { item ->
            localChannelDataSource.insertChannel(item)
        }
    }

    override suspend fun upsertChannelLocally(channel: Channel) {
        i("$TAG upsertChannelLocally()")
        channel.let { item ->
            localChannelDataSource.upsertChannel(item)
        }
    }

    /**
     * Used for generating samples
     */
    override suspend fun insertChannelsLocally(items: List<Channel>) {
        i("$TAG insertChannelsLocally() items=$items")
        items.let { listings ->
            localChannelDataSource.insertChannels(listings)
        }
    }

    override suspend fun toggleFollowingChannel(id: Int) {
        i("$TAG toggleFollowingChannel id=$id")
        localChannelDataSource.toggleFollowingChannel(id)
    }

    /////////////////////////////////////////////////////////////
    /////////////////////      REMOTE     ///////////////////////
    /////////////////////////////////////////////////////////////
    override suspend fun getChannelsFromRemote(): Flow<List<Channel>> {
        i("$TAG getChannelsFromRemote()")
        return remoteChannelDataSource.getChannels()
    }

    override suspend fun insertChannelRemotely(item: Channel) {
        i("$TAG insertChannelRemotely() item=$item")
        val x = remoteChannelDataSource.postChannel(item)
    }

    companion object {
        private const val TAG = "ChannelRepositoryImpl"
    }

}