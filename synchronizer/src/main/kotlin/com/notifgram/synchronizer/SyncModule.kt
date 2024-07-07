package com.notifgram.synchronizer

import com.notifgram.core.data_repository.sync.SyncManager
import com.notifgram.core.domain.usecase.StartSync
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.ktx.messaging
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Suppress("unused")
@Module
@InstallIn(SingletonComponent::class)
interface SyncModule {
    @Binds
    fun bindsSyncStatusMonitor(
        syncStatusMonitor: WorkManagerSyncManager,
    ): SyncManager

    @Binds
    fun bindsSyncSubscriber(
        syncSubscriber: FirebaseSyncSubscriber,
    ): SyncSubscriber

    @Binds
    fun bindsStartSync(
        startSync: StartSyncImpl,
    ): StartSync

    companion object {
        @Provides
        @Singleton
        fun provideFirebaseMessaging(): FirebaseMessaging = Firebase.messaging
    }
}
