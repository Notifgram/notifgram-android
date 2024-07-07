package com.notifgram.core.data_datastore.di

import com.notifgram.core.data_datastore.datasource.AppSettingsDataSourceImpl
import com.notifgram.core.data_repository.data_source.settings.AppSettingsDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Suppress("unused")
@Module
@InstallIn(SingletonComponent::class)
abstract class DatastoreDataSourceModule {

    @Binds
    abstract fun bindAppSettingsDataSource(appSettingsDataSourceImpl: AppSettingsDataSourceImpl): AppSettingsDataSource


}