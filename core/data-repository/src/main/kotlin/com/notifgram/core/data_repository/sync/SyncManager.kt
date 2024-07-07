package com.notifgram.core.data_repository.sync

import kotlinx.coroutines.flow.Flow

interface SyncManager {
    val isSyncing: Flow<Boolean>
    fun sync()
}
