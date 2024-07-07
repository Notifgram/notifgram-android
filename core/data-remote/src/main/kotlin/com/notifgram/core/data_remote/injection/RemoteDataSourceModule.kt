package com.notifgram.core.data_remote.injection

import com.notifgram.core.data_remote.networking.FileDownloaderImpl
import com.notifgram.core.data_remote.source.RemoteChannelDataSourceImpl
import com.notifgram.core.data_remote.source.RemotePostDataSourceImpl
import com.notifgram.core.data_repository.data_source.remote.FileDownloader
import com.notifgram.core.data_repository.data_source.remote.RemoteChannelDataSource
import com.notifgram.core.data_repository.data_source.remote.RemotePostDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Suppress("unused")
@Module
@InstallIn(SingletonComponent::class)
abstract class RemoteDataSourceModule {

    @Binds
    abstract fun bindRemoteChannelDataSource(remoteChannelDataSourceImpl: RemoteChannelDataSourceImpl): RemoteChannelDataSource

    @Binds
    abstract fun bindPostChannelDataSource(remotePostDataSourceImpl: RemotePostDataSourceImpl): RemotePostDataSource

    @Binds
    abstract fun bindFileDownloader(fileDownloaderImpl: FileDownloaderImpl): FileDownloader

}