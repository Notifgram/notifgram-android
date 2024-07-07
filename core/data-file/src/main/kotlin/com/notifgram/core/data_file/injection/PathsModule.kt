package com.notifgram.core.data_file.injection

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.io.File
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object PathsModule {

    @Provides
    @Singleton
    @Named("FilesDir")
    fun provideFilesDir(@ApplicationContext context: Context): File = context.filesDir

    @Provides
    @Singleton
    @Named("CachedFilesDir")
    fun provideCachedFilesDir(@Named("FilesDir") filesDir: File) =
        File(filesDir.path.plus("/cached"))

}