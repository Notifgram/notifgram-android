package com.notifgram.core.data_datastore

import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import com.notifgram.core.common.ApplicationScope
import com.notifgram.core.data_datastore.di.DataStoreModule
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.coroutines.CoroutineScope
import org.junit.rules.TemporaryFolder
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DataStoreModule::class],
)
object TestDataStoreModule {

    @Provides
    @Singleton
    fun providesUserPreferencesDataStore(
        @ApplicationScope scope: CoroutineScope,
        userPreferencesSerializer: AppSettingsSerializer,
        tmpFolder: TemporaryFolder,
    ): DataStore<AppSettings> =
        tmpFolder.testUserPreferencesDataStore(
            coroutineScope = scope,
            userPreferencesSerializer = userPreferencesSerializer,
        )
}

fun TemporaryFolder.testUserPreferencesDataStore(
    coroutineScope: CoroutineScope,
    userPreferencesSerializer: AppSettingsSerializer = AppSettingsSerializer(),
) = DataStoreFactory.create(
    serializer = userPreferencesSerializer,
    scope = coroutineScope,
) {
    newFile("user_preferences_test.pb")
}
