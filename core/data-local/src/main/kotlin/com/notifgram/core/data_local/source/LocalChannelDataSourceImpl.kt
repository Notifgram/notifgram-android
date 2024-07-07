package com.notifgram.core.data_local.source

import com.notifgram.core.common.MyLog.i
import com.notifgram.core.data_local.db.channel.ChannelDao
import com.notifgram.core.data_local.db.channel.toChannel
import com.notifgram.core.data_local.db.channel.toChannelEntity
import com.notifgram.core.data_repository.data_source.local.LocalChannelDataSource
import com.notifgram.core.domain.entity.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocalChannelDataSourceImpl @Inject constructor(
    private val dao: ChannelDao
) : LocalChannelDataSource {

    override suspend fun searchByName(searchText: String): List<Channel> =
        dao.searchByName(searchText).map { entity ->
            entity.toChannel()
        }

    // NOT USED
    override fun searchByNameFlow(searchText: String): Flow<List<Channel>> {
        return emptyFlow() // Not implemented yet
    }

    override fun getAllFlow(): Flow<List<Channel>> =
        dao.getAllFlow().map { list ->
            list.map { entity ->
                entity.toChannel()
            }
        }

    override suspend fun getAll(): List<Channel> =
        dao.getAll().map { entity ->
            entity.toChannel()
        }

    override fun getChannels(idsSet: Set<Int>): Flow<List<Channel>> {
        i("$TAG getChannels() set=$idsSet")
        return dao.getChannels(idsSet).map {
            it.map { entity ->
                entity.toChannel()
            }
        }
    }

    override suspend fun getChannel(id: Int): Channel? =
        dao.getChannelById(id)?.toChannel()

    override fun getChannelFlow(id: Int): Flow<Channel?> =
        dao.getChannelByIdFlow(id).map { it?.toChannel() }

    override fun getAllFollowedChannelsIdsFlow(): Flow<List<Int>> =
        dao.getAllFollowedChannelsIdsFlow()

    // Not used
    override suspend fun updateChannel(channel: Channel) =
        dao.updateChannel(channel.toChannelEntity())

    /**
     * Inserts or updates [Channel] in the db under the specified primary keys
     */
    override suspend fun upsertChannels(channels: List<Channel>) =
        dao.upsertChannels(channels.map { it.toChannelEntity() })


    override suspend fun insertChannel(item: Channel) {
        i("$TAG insertChannel() item=$item")
        dao.insertChannel(item.toChannelEntity())
    }

    override suspend fun upsertChannel(item: Channel) {
        i("$TAG upsertChannel() item=$item")
        dao.upsertChannel(item.toChannelEntity())
    }

    override suspend fun insertChannels(items: List<Channel>) {
        i("$TAG insertChannels() items=$items")
        dao.insertChannels(
            items.map { it.toChannelEntity() }
        )
    }

    override suspend fun delete(item: Channel): Int =
        dao.deleteChannel(item.toChannelEntity())

    override suspend fun delete(id: Int): Int =
        dao.deleteChannel(id)

    override suspend fun clearChannelListings() = dao.clearChannelListings()

    /**
     * for sync
     * Deletes rows in the db matching the specified [ids]
     */
    override suspend fun deleteChannels(ids: List<Int>): Int {
        i("$TAG deleteChannels() ids=$ids")
        val countOfDeletedRows = dao.deleteChannels(ids)
        i("$TAG deleteChannels() countOfDeletedRows=$countOfDeletedRows")
        return countOfDeletedRows
    }

    override suspend fun toggleFollowingChannel(id: Int) {
        i("$TAG toggleFollowingChannel id=$id")
        dao.toggleFollowingChannel(id)
    }

    companion object {
        private const val TAG = "LocalChannelDataSourceImpl"
    }
}