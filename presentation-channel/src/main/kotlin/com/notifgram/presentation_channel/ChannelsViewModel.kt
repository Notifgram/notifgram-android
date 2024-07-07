package com.notifgram.presentation_channel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.notifgram.core.common.MyLog.i
import com.notifgram.core.domain.entity.Result
import com.notifgram.core.domain.entity.doesMatchSearchQuery
import com.notifgram.core.domain.usecase.SyncUseCase
import com.notifgram.core.domain.usecase.channel.LoadLocalChannelsUseCase
import com.notifgram.core.domain.usecase.channel.ToggleFollowingChannelUseCase
import com.notifgram.presentation_channel.order.ChannelOrder
import com.notifgram.presentation_channel.order.OrderType
import com.notifgram.presentation_channel.order.orderChannels
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class OrderState(
    var channelOrder: ChannelOrder = ChannelOrder.Name(OrderType.Descending),
)

@HiltViewModel
class ChannelsViewModel @Inject constructor(
    loadLocalChannelsUseCase: LoadLocalChannelsUseCase,
    private val toggleFollowingChannelUseCase: ToggleFollowingChannelUseCase,
    private val syncUseCase: SyncUseCase,
//    private val generateSampleChannelsUseCase: GenerateSampleChannelsUseCase, // for debugging
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChannelListingsState())
    val uiState: StateFlow<ChannelListingsState> = _uiState

    private val _isOrderSectionVisible = MutableStateFlow(false)
    val isOrderSectionVisible = _isOrderSectionVisible.asStateFlow()

    private val _orderState = MutableStateFlow(OrderState())
    val orderState = _orderState.asStateFlow()

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    var allChannels: StateFlow<ChannelListingsState> =
        loadLocalChannelsUseCase.execute(LoadLocalChannelsUseCase.Request)
            .map {
                ChannelListingsState(items = (it as Result.Success).data.channels)
            }
            .catch { ex ->
                _uiState.value = ChannelListingsState(error = ex.message)
            }.onEach {
                i("$TAG allChannels.onEach{ it.searchText=${it.searchText}")
                i("$TAG allChannels.onEach{ it.items=${it.items}")
                i("$TAG allChannels.onEach{ it.isLoading=${it.isLoading}")
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = ChannelListingsState(isLoading = true),
            )

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val searchedChannels: StateFlow<ChannelListingsState> =
        _orderState.flatMapLatest { orderState1: OrderState ->

            searchText.debounce(500L)
                .onEach { _isSearching.update { true } }
                .combine(allChannels) { text, state ->
                    if (text.isBlank()) {
                        ChannelListingsState(
                            items = orderChannels(state.items, orderState1),
                            channelOrder = orderState1.channelOrder
                        )
                    } else {
                        ChannelListingsState(
                            items = orderChannels(
                                state.items.filter {
                                    it.doesMatchSearchQuery(text)
                                }, orderState1
                            ),
                            channelOrder = orderState1.channelOrder
                        )

                    }
                }
                .onEach { _isSearching.update { false } }

        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            initialValue = ChannelListingsState(isLoading = true)
        )

    fun onEvent(event: ChannelListingsEvent) {
        when (event) {
//            is ChannelListingsEvent.Refresh ->

            is ChannelListingsEvent.OnSearchQueryChange -> {
                i("$TAG ChannelListingsEvent.OnSearchQueryChange event.query=${event.query}")
                onSearchTextChange(event.query)
            }

            is ChannelListingsEvent.Order -> {
                if (_orderState.value.channelOrder::class == event.channelOrder::class &&
                    _orderState.value.channelOrder.orderType == event.channelOrder.orderType
                ) {
                    i("$TAG ChannelListingsEvent.Order -> {  order request is not changed.")
                    return
                }
                _orderState.value = _orderState.value.copy(channelOrder = event.channelOrder)
            }

            is ChannelListingsEvent.ToggleOrderSection ->
                _isOrderSectionVisible.value = !_isOrderSectionVisible.value

            is ChannelListingsEvent.ToggleFollowedChannel -> {
                i("$TAG ChannelListingsEvent.ToggleFollowedChannel  id=${event.id} event.newValue=${event.newValue}")
                viewModelScope.launch {
                    toggleFollowingChannelUseCase.execute(
                        ToggleFollowingChannelUseCase.Request(
                            event.id,
                            event.newValue
                        )
                    ).collect {}
                }
            }

            else -> {}
        }
    }

    fun sync() {
        i("$TAG sync")
        viewModelScope.launch {
            syncUseCase.execute(
                SyncUseCase.Request
            ).collect {}
        }
    }

    // gets triggered when onLongClick happens on channel card
    fun toggleSelectedChannel(channelId: Int) {
        val currentSelection = uiState.value.selectedChannels
        _uiState.value = _uiState.value.copy(
            selectedChannels = if (currentSelection.contains(channelId))
                currentSelection.minus(channelId) else currentSelection.plus(channelId)
        )
    }

    private fun onSearchTextChange(text: String) {
        _searchText.value = text
    }


    companion object {
        private const val TAG = "ChannelsViewModel"

    }
}

