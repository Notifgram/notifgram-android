package com.notifgram.core.data_repository.injection

import com.notifgram.core.domain.entity.settings.AppSettings
import com.notifgram.core.domain.repository.AppSettingsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class StartupModule {

    // Used on app startup
    @Singleton
    @Provides
    fun provideAppSettingsOnStartup(
        appSettingsRepository: AppSettingsRepository
    ): AppSettings = runBlocking { appSettingsRepository.appSettings.first() }

}