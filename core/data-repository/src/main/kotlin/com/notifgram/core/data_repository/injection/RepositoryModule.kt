package com.notifgram.core.data_repository.injection

import com.notifgram.core.data_repository.repository.AppSettingsRepositoryImpl
import com.notifgram.core.data_repository.repository.ChannelRepositoryImpl
import com.notifgram.core.data_repository.repository.PostRepositoryImpl
import com.notifgram.core.data_repository.repository.SyncableChannelRepository
import com.notifgram.core.data_repository.repository.SyncableChannelRepositoryImpl
import com.notifgram.core.data_repository.repository.SyncablePostRepository
import com.notifgram.core.data_repository.repository.SyncablePostRepositoryImpl
import com.notifgram.core.domain.repository.AppSettingsRepository
import com.notifgram.core.domain.repository.ChannelRepository
import com.notifgram.core.domain.repository.PostRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Suppress("unused")
@Module
@InstallIn(SingletonComponent::class)
internal abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindChannelRepository(
        channelRepositoryImpl: ChannelRepositoryImpl
    ): ChannelRepository

    @Binds
    @Singleton
    abstract fun bindSyncableChannelRepository(
        syncableChannelRepositoryImpl: SyncableChannelRepositoryImpl
    ): SyncableChannelRepository

    @Binds
    @Singleton
    abstract fun bindPostRepository(
        postRepositoryImpl: PostRepositoryImpl
    ): PostRepository

    @Binds
    @Singleton
    abstract fun bindSyncablePostRepository(
        syncablePostRepositoryImpl: SyncablePostRepositoryImpl
    ): SyncablePostRepository

    @Binds
    @Singleton
    abstract fun bindAppSettingsRepository(
        appSettingsRepositoryImpl: AppSettingsRepositoryImpl
    ): AppSettingsRepository

}