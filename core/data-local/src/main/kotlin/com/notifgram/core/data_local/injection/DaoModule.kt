package com.notifgram.core.data_local.injection

import com.notifgram.core.data_local.db.AppDatabase
import com.notifgram.core.data_local.db.channel.ChannelDao
import com.notifgram.core.data_local.db.post.PostDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal object DaoModule {

    @Provides
    fun provideChannelDao(appDatabase: AppDatabase): ChannelDao =
        appDatabase.channelDao

    @Provides
    fun providePostDao(appDatabase: AppDatabase): PostDao =
        appDatabase.postDao

}


