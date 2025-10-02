package com.notifgram.presentation_post

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.notifgram.core.common.MyLog.i
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch

private const val TAG = "PostsListViewerComp"

@OptIn(
     InternalCoroutinesApi::class,
    ExperimentalCoroutinesApi::class, ExperimentalMaterial3Api::class
)
@Composable
internal fun PostsListViewerComp(
//    onPostClick: (post: Post) -> Unit,
//    onFollowPostClick: (Int) -> Unit,
    postsState: PostListingsState,
    searchTextState: String,
    isSearchingState: Boolean,
    uiState: PostListingsState,
    onPullRefresh: () -> Unit,
    onPostLongClick: (Int) -> Unit,
//    debug: PostListingsState,
    ) {
    i("$TAG called")

    val refreshScope = rememberCoroutineScope()
    var refreshing by rememberSaveable { mutableStateOf(false) }
    val pullRefreshState = rememberPullToRefreshState()

    if (isSearchingState) LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
//    val sortedItems = postsState.sortedBy { it.isEnable }

    if (postsState.items.isEmpty())
        NoSearchResultsComp()
    else
        PullToRefreshBox(
            isRefreshing = refreshing,
            onRefresh = {
                refreshScope.launch {
                    refreshing = true
                    onPullRefresh()
                    refreshing = false
                }
            },
            state = pullRefreshState,
            modifier = Modifier.fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(15.dp, 0.dp, 15.dp, 0.dp)
            ) {
                if (!refreshing) {
                    items(postsState.items.size) { i ->
                        val post = postsState.items[i]
                        Row {
                            PostCardBuilder(
                                post = post,
                                onPostClick = {
                                    i("onPostClick. STATE=${postsState.items.size}")
                                    i("onPostClick. post.id.toString()=${post.id}")
//                                onPostClick(post)
                                },
                                isSelected = uiState.selectedPosts.contains(post.id),
                                onPostLongClick = { onPostLongClick(post.id) },
//                            onFollowPostClick = { onFollowPostClick(post.id) },
                            )
                        }

                        if (i < postsState.items.size - 1) {
                            HorizontalDivider(
                                modifier = Modifier.padding(
                                    vertical = 6.dp
                                )
                            )
                        }
                    }
                }// End if
            }// end LazyColumn

        }
}