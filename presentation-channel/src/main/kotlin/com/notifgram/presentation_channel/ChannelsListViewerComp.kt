package com.notifgram.presentation_channel

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.notifgram.core.common.MyLog.i
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch

private const val TAG = "ChannelsListViewerComp"

@OptIn(
    ExperimentalMaterialApi::class, InternalCoroutinesApi::class,
    ExperimentalCoroutinesApi::class
)
@Composable
internal fun ChannelsListViewerComp(
//    onChannelClick: (channel: Channel) -> Unit,
    onFollowChannelClick: (Int, Boolean) -> Unit,
    channelsState: ChannelListingsState,
    searchTextState: String,
    isSearchingState: Boolean,
    uiState: ChannelListingsState,
    onPullRefresh: () -> Unit,
    onChannelLongClick: (Int) -> Unit,

    ) {
    i("$TAG called")

    val refreshScope = rememberCoroutineScope()
    var refreshing by rememberSaveable { mutableStateOf(false) }

    val pullRefreshState = rememberPullRefreshState(
        refreshing = refreshing,
        onRefresh = {
            refreshScope.launch {
                refreshing = true
//                delay(1500)  // TODO: Remove this line
//                viewModel.onEvent(ChannelListingsEvent.Refresh)
                onPullRefresh()
                refreshing = false
            }
        })

    if (isSearchingState) LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
//    val sortedItems = channelsState.sortedBy { it.isEnable }

    if (channelsState.items.isEmpty())
        NoSearchResultsComp()
    Box(Modifier.pullRefresh(pullRefreshState)) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(15.dp, 0.dp, 15.dp, 0.dp)
        ) {
            if (!refreshing) {
                items(channelsState.items.size) { i ->
                    val channel = channelsState.items[i]
                    Row {
                        ChannelCardBuilder(
                            channel = channel,
                            onChannelClick = {
                                i("onChannelClick. STATE=${channelsState.items.size}")
                                i("onChannelClick. channel.id.toString()=${channel.id}")
//                                onChannelClick(channel)
                            },
                            isSelected = uiState.selectedChannels.contains(channel.id),
                            onChannelLongClick = { onChannelLongClick(channel.id) },
                            onFollowChannelClick = {
                                onFollowChannelClick(
                                    channel.id,
                                    !channel.isFollowed
                                )
                            },
                        )
                    }

                    if (i < channelsState.items.size - 1) {
                        HorizontalDivider(
                            modifier = Modifier.padding(
                                vertical = 6.dp
                            )
                        )
                    }
                }
            }// End if
        }// end LazyColumn
        PullRefreshIndicator(refreshing, pullRefreshState, Modifier.align(Alignment.TopCenter))
    }//end Box

}

@Composable
fun NoSearchResultsComp() {

    Column(
        modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center,
        horizontalAlignment = CenterHorizontally
    ) {
        Text("Not found")
    }
}