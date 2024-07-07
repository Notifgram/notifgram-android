package com.notifgram.core.data_repository.data_source.settings

import com.notifgram.core.domain.entity.settings.AppSettings
import kotlinx.coroutines.flow.Flow

interface AppSettingsDataSource {
    val userData: Flow<AppSettings>
    suspend fun updateIsAuthSupported(value: Boolean)
    suspend fun toggleShouldEncrypt()
    suspend fun toggleShouldEncryptChunks()
    suspend fun toggleIsAuthEnabled()
    suspend fun toggleFcmUsageEnabled()
}