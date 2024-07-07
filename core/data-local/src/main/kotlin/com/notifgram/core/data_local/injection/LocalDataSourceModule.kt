package com.notifgram.core.data_local.injection

import com.notifgram.core.data_local.source.LocalChannelDataSourceImpl
import com.notifgram.core.data_local.source.LocalPostDataSourceImpl
import com.notifgram.core.data_repository.data_source.local.LocalChannelDataSource
import com.notifgram.core.data_repository.data_source.local.LocalPostDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Suppress("unused")
@Module
@InstallIn(SingletonComponent::class)
abstract class LocalDataSourceModule {

    @Binds
    abstract fun bindLocalChannelDataSource(localChannelDataSourceImpl: LocalChannelDataSourceImpl): LocalChannelDataSource

    @Binds
    abstract fun bindLocalPostDataSource(localPostDataSourceImpl: LocalPostDataSourceImpl): LocalPostDataSource

    //////////////////////////////////////////////////////////////////////////
    /////////////////////////////      SYNC       ////////////////////////////
    //////////////////////////////////////////////////////////////////////////


}