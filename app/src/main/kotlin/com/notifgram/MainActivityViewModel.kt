package com.notifgram

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.notifgram.core.common.FirebaseCloudMessaging
import com.notifgram.core.domain.entity.settings.AppSettings
import com.notifgram.core.domain.repository.AppSettingsRepository
import com.notifgram.core.domain.repository.ChannelRepository
import com.notifgram.MainActivityUiState.Loading
import com.notifgram.MainActivityUiState.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val appSettingsRepository: AppSettingsRepository,
    private val channelRepository: ChannelRepository,
) : ViewModel() {

    val uiState: StateFlow<MainActivityUiState> = appSettingsRepository.appSettings.map {
        Success(it)
    }.stateIn(
        initialValue = Loading,
        started = SharingStarted.WhileSubscribed(5_000),
        scope = viewModelScope,
    )

    fun updateIsAuthSupported(newValue: Boolean) {
        viewModelScope.launch {
            appSettingsRepository.updateIsAuthSupported(newValue)
        }
    }

    fun subscribeToFollowedChannels() {
        viewModelScope.launch {
            channelRepository.getAllFollowedChannelsIdsFlow().collect { followedChannels ->
                followedChannels.forEach { channelId ->
                    FirebaseCloudMessaging().subscribeToTopic(channelId.toString())
                }
            }
        }
    }

}


sealed interface MainActivityUiState {
    data class Success(val appSettings: AppSettings) : MainActivityUiState
    data object Loading : MainActivityUiState
}
