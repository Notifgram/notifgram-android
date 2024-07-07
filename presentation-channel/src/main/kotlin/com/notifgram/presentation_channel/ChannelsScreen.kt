package com.notifgram.presentation_channel

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.notifgram.core.common.MyLog.i
import com.notifgram.core.domain.entity.Channel
import com.notifgram.core.presentation_core.utils.DevicePreviews
import com.notifgram.core.presentation_core.utils.WindowInfo
import com.notifgram.presentation_channel.order.ChannelOrder

private const val TAG = "ChannelsScreen"

@Composable
fun ChannelsScreen(
    viewModel: ChannelsViewModel = hiltViewModel(),
    navController: NavController,
    windowInfo: WindowInfo
) {
    i("$TAG called")

    val allChannelsState by viewModel.allChannels.collectAsStateWithLifecycle()
    val channelsState by viewModel.searchedChannels.collectAsStateWithLifecycle()
    val searchTextState by viewModel.searchText.collectAsState()
    val isSearchingState by viewModel.isSearching.collectAsState()

    val isOrderSectionVisible by viewModel.isOrderSectionVisible.collectAsStateWithLifecycle()
    val orderState by viewModel.orderState.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsState()

    val snackBarHostState = remember { SnackbarHostState() }

    val onChannelCardClick = { channel: Channel ->
//        i("$TAG onChannelCardClick ${channel.id}")
//        navController.navigate(
//            Screen.ShowChannelDetails.createRouteWithParameter(
//                channel.id.toString()
//            )
//        )
//        i("$TAG onChannelCardClick end")

    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
        //////// START FLOATING ACTION BAR //////////
        floatingActionButton = {
            FloatingActionButton(
                elevation = FloatingActionButtonDefaults.elevation(0.dp),
                onClick = {  }
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "")
            }
        },
        floatingActionButtonPosition = FabPosition.End
        //////// END FLOATING ACTION BAR //////////
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            ChannelsScreenContent(
                allChannelsState = allChannelsState,
                uiState = uiState,
                channelsState = channelsState,
                searchTextState = searchTextState,
                isSearchingState = isSearchingState,
                orderState = orderState,
                windowInfo = windowInfo,
                onPullRefresh = viewModel::sync,
                onChannelCardClick = onChannelCardClick,
                onChannelLongClick = { channelId: Int ->
                    viewModel.toggleSelectedChannel(channelId)
                },
                channelsSearchBar = {
                    ChannelsSearchBar { it: String ->
                        viewModel.onEvent(ChannelListingsEvent.OnSearchQueryChange(it))
                    }
                },
                sortComp = {
                    SortComp(
                        isOrderSectionVisible = isOrderSectionVisible,
                        orderState = orderState,
                        onOrderChange = { it: ChannelOrder ->
                            viewModel.onEvent(ChannelListingsEvent.Order(it))
                        },
                        onToggleVisibilityOfSortBarButtonClick = {
                            viewModel.onEvent(
                                ChannelListingsEvent.ToggleOrderSection
                            )
                        }
                    )
                },
                onFollowChannelClick = { channelId: Int, newValue: Boolean ->
                    viewModel.onEvent(
                        ChannelListingsEvent.ToggleFollowedChannel(
                            channelId,
                            newValue
                        )
                    )
                }
            )
        }
    }

}

@Composable
private fun SortComp(
    isOrderSectionVisible: Boolean,
    onToggleVisibilityOfSortBarButtonClick: () -> Unit,
    orderState: OrderState,
    onOrderChange: (ChannelOrder) -> Unit,
) {

    ToggleVisibilityOfSortBarButton(
        isOrderSectionVisible = isOrderSectionVisible,
        onClick = onToggleVisibilityOfSortBarButtonClick
    )

    ChannelsSortBar(
        isOrderSectionVisible = isOrderSectionVisible,
        orderState = orderState,
        showCompactRow = false,
        onOrderChange = onOrderChange
    )

}

@Composable
private fun ChannelsScreenContent(
    windowInfo: WindowInfo,
    onChannelCardClick: (Channel) -> Unit,
    allChannelsState: ChannelListingsState,
    channelsState: ChannelListingsState,
    isSearchingState: Boolean,
    searchTextState: String,
    orderState: OrderState,
    onPullRefresh: () -> Unit,
    sortComp: @Composable (() -> Unit)? = null,
    onFollowChannelClick: (Int, Boolean) -> Unit,
    channelsSearchBar: @Composable (() -> Unit)? = null,
    uiState: ChannelListingsState,
    onChannelLongClick: (Int) -> Unit,
) {

    Column(modifier = Modifier.fillMaxHeight()) {
        channelsSearchBar?.invoke()
        sortComp?.invoke()

        ChannelsListViewerComp(
            channelsState = channelsState,
//                    onChannelCardClick,
            searchTextState = searchTextState,
            isSearchingState = isSearchingState,
            uiState = uiState,
            onPullRefresh = onPullRefresh,
            onFollowChannelClick = onFollowChannelClick,
            onChannelLongClick = onChannelLongClick
        )
    }

}


//@Preview(
//    "dark theme",
//    locale = "ar",
//    device = Devices.AUTOMOTIVE_1024p,
//    widthDp = 1080,
//    heightDp = 760,
//    uiMode = Configuration.UI_MODE_NIGHT_YES,
//)
//@Preview(
//    "light theme",
//    showBackground = true,
//    locale = "ar",
//    device = Devices.AUTOMOTIVE_1024p,
//    widthDp = 720,
//    heightDp = 360,
//)
@DevicePreviews
@Composable
private fun ChannelsScreenPreview() {
    MaterialTheme {
//        ChannelsScreen()
    }
}