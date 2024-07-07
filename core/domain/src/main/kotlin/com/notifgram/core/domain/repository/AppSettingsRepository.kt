package com.notifgram.core.domain.repository

import com.notifgram.core.domain.entity.settings.AppSettings
import kotlinx.coroutines.flow.Flow

interface AppSettingsRepository {

    val appSettings: Flow<AppSettings>

    /**
     * updates the value for "Is authentication enabled for the app"
     */
    suspend fun toggleIsAuthEnabled()

    /**
     * updates the value for "Does the devices support authentication"
     */
    suspend fun updateIsAuthSupported(newValue: Boolean)

    /**
     * updates the value for "Does user request cached files encryption"
     */
    suspend fun toggleShouldEncrypt()

    /**
     * updates the value for "Does user request cached file chunks encryption"
     */
    suspend fun toggleShouldEncryptChunks()

    /**
     * updates the value for "Does user prefer to receive data over FCM"
     */
    suspend fun toggleFcmUsageEnabled()
}
