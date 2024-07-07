package com.notifgram.core.data_datastore.datasource

import androidx.datastore.core.DataStore
import com.notifgram.core.common.MyLog.e
import com.notifgram.core.common.MyLog.i
import com.notifgram.core.data_datastore.AppSettings
import com.notifgram.core.data_datastore.copy
import com.notifgram.core.data_repository.data_source.settings.AppSettingsDataSource
import com.notifgram.core.data_repository.sync.CachedVersions
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class AppSettingsDataSourceImpl @Inject constructor(
    private val appSettings: DataStore<AppSettings>,
) : AppSettingsDataSource {
    override val userData = appSettings.data
        .map {
            com.notifgram.core.domain.entity.settings.AppSettings(
                isAuthEnabled = it.isAuthEnabled,
                isAuthSupported = it.isAuthSupported,
                shouldEncrypt = it.shouldEncrypt,
                shouldEncryptChunks = it.shouldEncryptChunks,
                encryptedSqlCipherPassphrase = it.encryptedSqlCipherPassphrase,
                isFcmUsageEnabled = it.isFcmUsageEnabled
            )
        }

    override suspend fun updateIsAuthSupported(value: Boolean) {
        i("$TAG updateIsAuthSupported")
        try {
            appSettings.updateData {
                it.copy {
                    this.isAuthSupported = value
                }
            }
        } catch (ioException: IOException) {
            e("$TAG Failed to change user setting", ioException)
        }
    }

    override suspend fun toggleShouldEncrypt() {
        i("$TAG toggleShouldEncrypt")
        try {
            appSettings.updateData { it ->
                it.copy {
                    this.shouldEncrypt = !it.shouldEncrypt
                }
            }
        } catch (ioException: IOException) {
            e("$TAG Failed to change user setting", ioException)
        }
    }

    override suspend fun toggleShouldEncryptChunks() {
        i("$TAG toggleShouldEncryptChunks")
        try {
            appSettings.updateData { it ->
                it.copy {
                    this.shouldEncryptChunks = !it.shouldEncryptChunks
                }
            }
        } catch (ioException: IOException) {
            e("$TAG Failed to change user setting", ioException)
        }
    }

    override suspend fun toggleIsAuthEnabled() {
        i("$TAG toggleIsAuthEnabled")
        try {
            appSettings.updateData { it ->
                it.copy {
                    this.isAuthEnabled = !it.isAuthEnabled
                }
            }
        } catch (ioException: IOException) {
            e("$TAG Failed to change user setting", ioException)
        }
    }

    override suspend fun toggleFcmUsageEnabled() {
        i("$TAG toggleFcmUsageEnabled")
        try {
            appSettings.updateData { it ->
                it.copy {
                    this.isFcmUsageEnabled = !it.isFcmUsageEnabled
                }
            }
        } catch (ioException: IOException) {
            e("$TAG Failed to change user setting", ioException)
        }
    }

    //    suspend fun getEncryptedSqlcipherPassphrase() = appSettings.data.map {
//        it.encryptedSqlCipherPassphrase
//    }
    suspend fun updateEncryptedSqlcipherPassphrase(passphrase: String) {
        i("$TAG updateEncryptedSqlcipherPassphrase")
        try {
            appSettings.updateData { currentPreferences ->
                currentPreferences.copy {
                    encryptedSqlCipherPassphrase = passphrase
                }
            }
        } catch (ioException: IOException) {
            e("$TAG Failed to change user setting", ioException)
        }
    }
    suspend fun getChangeListVersions() = appSettings.data
        .map {
            CachedVersions(
                channelVersion = it.channelLastRemoteChange,
                postVersion = it.postLastRemoteChange
            )
        }
        .firstOrNull() ?: CachedVersions()


    suspend fun updateChangeListVersion(update: CachedVersions.() -> CachedVersions) {
        i("$TAG updateChangeListVersion")
        try {
            appSettings.updateData { currentPreferences ->
                val updatedCachedVersions = update(
                    CachedVersions(
                        channelVersion = currentPreferences.channelLastRemoteChange,
                        postVersion = currentPreferences.postLastRemoteChange
                    ),
                )

                currentPreferences.copy {
                    channelLastRemoteChange = updatedCachedVersions.channelVersion
                    postLastRemoteChange = updatedCachedVersions.postVersion
                }
            }
        } catch (ioException: IOException) {
            e("$TAG Failed to change user setting", ioException)
        }
    }

    companion object {
        private const val TAG = "AppSettingsDataSourceImpl"
    }

}