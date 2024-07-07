package com.notifgram.core.data_repository.repository

import com.notifgram.core.common.MyLog.i
import com.notifgram.core.data_repository.data_source.local.LocalChannelDataSource
import com.notifgram.core.data_repository.data_source.remote.RemoteChannelDataSource
import com.notifgram.core.data_repository.sync.CachedVersions
import com.notifgram.core.data_repository.sync.Synchronizer
import com.notifgram.core.data_repository.sync.changeListSync
import com.notifgram.core.domain.entity.Channel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SyncableChannelRepositoryImpl @Inject constructor(
    private val localChannelRoomDataSource: LocalChannelDataSource,
    private val remoteChannelDataSource: RemoteChannelDataSource,
//    private val notifier: Notifier,
) : SyncableChannelRepository {

    override fun getChannels(): Flow<List<Channel>> = localChannelRoomDataSource.getAllFlow()

    override suspend fun syncWith(synchronizer: Synchronizer): Boolean {
        i("$TAG syncWith")
        return synchronizer.changeListSync(
            versionReader = CachedVersions::channelVersion,
            changeListFetcherFromRemote = { currentVersion ->
                remoteChannelDataSource.getChannelChangeList(after = currentVersion)
            },
            versionUpdater = { latestVersion ->
                copy(channelVersion = latestVersion)
            },
            modelDeleter = localChannelRoomDataSource::deleteChannels,
            modelUpdater = { changedIds ->
                changedIds.chunked(50).forEach { chunkedIds ->
                    val remoteChannels = remoteChannelDataSource.getChannels(ids = chunkedIds)

                    localChannelRoomDataSource.upsertChannels(channels = remoteChannels)

                }
            },
        )
    }

    companion object {
        private const val TAG = "SyncableChannelRepositoryImpl"
    }
}