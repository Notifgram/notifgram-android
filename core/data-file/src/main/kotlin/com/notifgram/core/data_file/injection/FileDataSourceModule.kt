package com.notifgram.core.data_file.injection

import com.notifgram.core.data_file.source.FileDataSourceImpl
import com.notifgram.core.data_repository.data_source.file.FileDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Suppress("unused")
@Module
@InstallIn(SingletonComponent::class)
abstract class FileDataSourceModule {

    @Binds
    abstract fun bindFileDataSource(fileDataSourceImpl: FileDataSourceImpl): FileDataSource

}