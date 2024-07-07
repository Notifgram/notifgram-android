package com.notifgram.synchronizer

import android.content.Context
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import com.notifgram.core.common.MyLog.i
import com.notifgram.core.domain.usecase.StartSync
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

fun startSync(context: Context) {
    i("startSync")
    WorkManager.getInstance(context)
        .apply {
            enqueueUniqueWork(
                context.getString(R.string.syncworkmanagername),
                ExistingWorkPolicy.KEEP,
                SyncWorker.startUpSyncWork(),
            )
        }
}

class StartSyncImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : StartSync {
    override fun startSync() {
        startSync(context)
    }
}