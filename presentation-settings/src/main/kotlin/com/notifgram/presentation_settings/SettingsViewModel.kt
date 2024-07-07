package com.notifgram.presentation_settings

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.notifgram.core.common.MyLog.i
import com.notifgram.core.data_repository.data_source.file.FileDataSource
import com.notifgram.core.domain.entity.settings.AppSettings
import com.notifgram.core.domain.repository.AppSettingsRepository
import com.notifgram.core.domain.usecase.channel.DeleteAllLocalChannelsUseCase
import com.notifgram.core.domain.usecase.post.DeleteAllLocalPostsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.system.exitProcess
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val appSettingsRepository: AppSettingsRepository,
    private val fileDataSource: FileDataSource,
    private val deleteAllLocalChannelsUseCase: DeleteAllLocalChannelsUseCase,
    private val deleteAllLocalPostsUseCase: DeleteAllLocalPostsUseCase,

    ) : ViewModel() {
    val settingsUiState: StateFlow<SettingsUiState> =
        appSettingsRepository.appSettings
            .map { userData ->
                SettingsUiState.Success(
                    appSettings = AppSettings(
                        isAuthEnabled = userData.isAuthEnabled,
                        shouldEncryptChunks = userData.shouldEncryptChunks,
                        isAuthSupported = userData.isAuthSupported,
                        shouldEncrypt = userData.shouldEncrypt,
                        encryptedSqlCipherPassphrase = userData.encryptedSqlCipherPassphrase,
                        isFcmUsageEnabled = userData.isFcmUsageEnabled
                    ),
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = WhileSubscribed(5.seconds.inWholeMilliseconds),
                initialValue = SettingsUiState.Loading,
            )

    fun toggleShouldEncrypt() {
        i("$TAG toggleShouldEncrypt")
        deleteCachedFilesDirectory()
        viewModelScope.launch {
            appSettingsRepository.toggleShouldEncrypt()
        }
    }

    fun toggleShouldEncryptChunks() {
        i("$TAG toggleShouldEncryptChunks")
        deleteCachedFilesDirectory()
        viewModelScope.launch {
            appSettingsRepository.toggleShouldEncryptChunks()
        }
    }

    fun toggleIsAuthEnabled() {
        i("$TAG toggleIsAuthEnabled")
        viewModelScope.launch {
            appSettingsRepository.toggleIsAuthEnabled()
        }
    }

    fun toggleFcmUsageEnabled() {
        i("$TAG toggleFcmUsageEnabled")
        viewModelScope.launch {
            appSettingsRepository.toggleFcmUsageEnabled()
        }
    }

    /**
     * closes the app and opens again
     */
    fun restartApp(context: Context) {
        val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
        intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        (context as Activity).finish()
        context.startActivity(intent)
        exitProcess(0)
    }

    fun deleteCachedFilesDirectory() {
        i("$TAG deleteCachedFilesDirectory")
        fileDataSource.deleteCachedFilesDirectory()
    }

    fun clearLocalDatabase() {
        i("$TAG clearLocalDatabase")
        viewModelScope.launch {
            deleteAllLocalPostsUseCase.invoke()
            deleteAllLocalChannelsUseCase.invoke()
        }
    }

    fun sendEmail(context: Context, token: String) {
        val backendEmailAddress = ""
        val emailSubject = "channels.$token"
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_EMAIL, arrayOf(backendEmailAddress))
            putExtra(Intent.EXTRA_SUBJECT, emailSubject)
            putExtra(Intent.EXTRA_TEXT, "Email body is ignored. Do not change subject.")
        }

        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(Intent.createChooser(intent, "Send Email"))
        }
    }

    companion object {
        private const val TAG = "SettingsViewModel"
    }
}

sealed interface SettingsUiState {
    data object Loading : SettingsUiState
    data class Success(val appSettings: AppSettings) : SettingsUiState
}
