package com.notifgram.synchronizer

import android.content.Context
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.notifgram.core.common.MyLog.i
import com.notifgram.core.data_repository.sync.SyncManager
import com.notifgram.core.domain.entity.settings.AppSettings
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class WorkManagerSyncManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val appSettings: AppSettings
) : SyncManager {

    override val isSyncing: Flow<Boolean> =
        WorkManager.getInstance(context)
            .getWorkInfosForUniqueWorkFlow(R.string.syncworkmanagername.toString())
            .map(List<WorkInfo>::anyRunning)
            .conflate()

    override fun sync() {
        i("$TAG requestSync() appSettings.isFcmUsageEnabled=${appSettings.isFcmUsageEnabled}")

        if (!appSettings.isFcmUsageEnabled)
            startSync(context)
    }

    companion object {
        private const val TAG = "WorkManagerSyncManager"
    }
}

private fun List<WorkInfo>.anyRunning() = any { it.state == WorkInfo.State.RUNNING }
