package com.notifgram.core.data_datastore.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.notifgram.core.common.AppDispatchers
import com.notifgram.core.common.ApplicationScope
import com.notifgram.core.common.Dispatcher
import com.notifgram.core.data_datastore.AppSettings
import com.notifgram.core.data_datastore.AppSettingsSerializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    @Singleton
    fun providesAppSettingsDataStore(
        appSettingsSerializer: AppSettingsSerializer,
        @ApplicationContext context: Context,
        @ApplicationScope scope: CoroutineScope,
        @Dispatcher(AppDispatchers.IO) ioDispatcher: CoroutineDispatcher,
    ): DataStore<AppSettings> =
        DataStoreFactory.create(
            serializer = appSettingsSerializer,
            migrations = listOf(),
            scope = CoroutineScope(scope.coroutineContext + ioDispatcher),
        ) {
            context.dataStoreFile("app_settings.pb")
        }

}
