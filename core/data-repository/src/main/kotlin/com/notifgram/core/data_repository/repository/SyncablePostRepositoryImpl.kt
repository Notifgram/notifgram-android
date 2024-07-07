package com.notifgram.core.data_repository.repository

import android.net.Uri
import com.notifgram.core.common.BACKEND_FILES_URL
import com.notifgram.core.common.MyLog.i
import com.notifgram.core.data_repository.data_source.local.LocalPostDataSource
import com.notifgram.core.data_repository.data_source.remote.FileDownloader
import com.notifgram.core.data_repository.data_source.remote.RemotePostDataSource
import com.notifgram.core.data_repository.sync.CachedVersions
import com.notifgram.core.data_repository.sync.Synchronizer
import com.notifgram.core.data_repository.sync.changeListSync
import com.notifgram.core.domain.entity.Post
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SyncablePostRepositoryImpl @Inject constructor(
    private val localPostRoomDataSource: LocalPostDataSource,
    private val remotePostDataSource: RemotePostDataSource,
    private val fileDownloader: FileDownloader,
//    private val notifier: Notifier,
) : SyncablePostRepository {

    override fun getPosts(): Flow<List<Post>> = localPostRoomDataSource.getAllFlow()

    override suspend fun syncWith(synchronizer: Synchronizer): Boolean {
        i("$TAG syncWith")
        return synchronizer.changeListSync(
            versionReader = CachedVersions::postVersion,
            changeListFetcherFromRemote = { currentVersion ->
                remotePostDataSource.getPostChangeList(after = currentVersion)
            },
            versionUpdater = { latestVersion ->
                copy(postVersion = latestVersion)
            },
            modelDeleter = localPostRoomDataSource::deletePosts,
            modelUpdater = { changedIds ->
                changedIds.chunked(50).forEach { chunkedIds ->
                    val remotePosts = remotePostDataSource.getPosts(ids = chunkedIds)
                    i("$TAG syncWith() remotePosts.count()=${remotePosts.count()}")
                    localPostRoomDataSource.upsertPosts(items = remotePosts)

                    remotePosts.forEach { post ->
                        i("$TAG syncWith() fileDownloadUrl")

                        post.mediaFile?.let { file ->
                            val fileName = Uri.parse(file.path).lastPathSegment
                            val fileDownloadUrl = "$BACKEND_FILES_URL/$fileName"
                            fileDownloader.addToQueue(fileDownloadUrl)
                            i("$TAG syncWith() fileDownloadUrl=$fileDownloadUrl")
                        }

                    }
                }
            },
        )
    }

    companion object {
        private const val TAG = "SyncablePostRepositoryImpl"
    }
}