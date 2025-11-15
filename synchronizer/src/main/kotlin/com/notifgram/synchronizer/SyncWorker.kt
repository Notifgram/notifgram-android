package com.notifgram.synchronizer

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.tracing.traceAsync
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkerParameters
import com.notifgram.core.common.AppDispatchers
import com.notifgram.core.common.Dispatcher
import com.notifgram.core.common.MyLog.i
import com.notifgram.core.data_datastore.datasource.AppSettingsDataSourceImpl
import com.notifgram.core.data_repository.repository.SyncableChannelRepository
import com.notifgram.core.data_repository.repository.SyncablePostRepository
import com.notifgram.core.data_repository.sync.CachedVersions
import com.notifgram.core.data_repository.sync.Synchronizer
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext


@HiltWorker
internal class SyncWorker @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val appSettingsDataSourceImpl: AppSettingsDataSourceImpl,
    private val syncableChannelRepository: SyncableChannelRepository,
    private val syncablePostRepository: SyncablePostRepository,
    @Dispatcher(AppDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
) : CoroutineWorker(appContext, workerParams), Synchronizer {

    override suspend fun doWork(): Result = withContext(ioDispatcher) {
        i("$TAG doWork()")
        traceAsync("Sync", 0) {
            i("$TAG doWork() traceAsync()")

            val syncedSuccessfully = awaitAll(
                async { syncableChannelRepository.sync() },
                async { syncablePostRepository.sync() },

            ).all { it }

            if (syncedSuccessfully) {
                Result.success()
            } else {
                Result.retry()
            }
        }
    }

    override suspend fun getChangeListVersions(): CachedVersions =
        appSettingsDataSourceImpl.getChangeListVersions()

    override suspend fun updateChangeListVersions(
        update: CachedVersions.() -> CachedVersions,
    ) = appSettingsDataSourceImpl.updateChangeListVersion(update)

    companion object {
        private const val TAG = "SyncWorker"

        fun startUpSyncWork() = OneTimeWorkRequestBuilder<DelegatingWorker>()
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .setConstraints(SyncConstraints)
            .setInputData(SyncWorker::class.delegatedData())
            .build()
    }
}

val SyncConstraints
    get() = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()