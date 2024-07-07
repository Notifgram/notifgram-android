package com.notifgram.core.data_remote.injection

import android.content.Context
import com.notifgram.core.data_remote.networking.FileDownloaderImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object FileDownloaderModule {
    @Provides
    @Singleton
    fun provideFileDownloader(@ApplicationContext context: Context) =
        FileDownloaderImpl(context)
}