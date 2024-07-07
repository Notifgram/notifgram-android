package com.notifgram.core.data_repository.repository

import com.notifgram.core.data_repository.data_source.settings.AppSettingsDataSource
import com.notifgram.core.domain.entity.settings.AppSettings
import com.notifgram.core.domain.repository.AppSettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AppSettingsRepositoryImpl @Inject constructor(
    private val appSettingsDataSource: AppSettingsDataSource,
) : AppSettingsRepository {

    override val appSettings: Flow<AppSettings> =
        appSettingsDataSource.userData

    override suspend fun toggleIsAuthEnabled() {
        appSettingsDataSource.toggleIsAuthEnabled()
    }

    override suspend fun updateIsAuthSupported(newValue: Boolean) {
        appSettingsDataSource.updateIsAuthSupported(newValue)
    }

    override suspend fun toggleShouldEncrypt() {
        appSettingsDataSource.toggleShouldEncrypt()
    }

    override suspend fun toggleShouldEncryptChunks() {
        appSettingsDataSource.toggleShouldEncryptChunks()
    }

    override suspend fun toggleFcmUsageEnabled() {
        appSettingsDataSource.toggleFcmUsageEnabled()
    }

}
