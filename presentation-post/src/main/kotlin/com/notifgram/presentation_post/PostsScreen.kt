package com.notifgram.presentation_post

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
import com.notifgram.core.domain.entity.Post
import com.notifgram.core.presentation_core.utils.DevicePreviews
import com.notifgram.core.presentation_core.utils.WindowInfo

private const val TAG = "PostsScreen"

@Composable
fun PostsScreen(
    viewModel: PostsViewModel = hiltViewModel(),
    navController: NavController,
    windowInfo: WindowInfo
) {
    i("$TAG called")

//    val allPostsState by viewModel.allPosts.collectAsStateWithLifecycle()
//    val debug by viewModel.post.collectAsStateWithLifecycle()
    val postsState by viewModel.allPosts.collectAsStateWithLifecycle() // TODO: allPosts?
    i("$TAG postsState.items=${postsState.items}")
    val searchTextState by viewModel.searchText.collectAsState()
    val isSearchingState by viewModel.isSearching.collectAsState()

    val isOrderSectionVisible by viewModel.isOrderSectionVisible.collectAsStateWithLifecycle()
//    val orderState by viewModel.orderState.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsState()
    i("$TAG uiState.items=${uiState.items}")

    val snackBarHostState = remember { SnackbarHostState() }
    val onPostCardClick = { post: Post ->
//        i("$TAG onPostCardClick ${post.id}")
//        navController.navigate(
//            Screen.ShowPostDetails.createRouteWithParameter(
//                post.id.toString()
//            )
//        )
//        i("$TAG onPostCardClick end")

    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
        //////// START FLOATING ACTION BAR //////////
        floatingActionButton = {
            FloatingActionButton(
                elevation = FloatingActionButtonDefaults.elevation(0.dp),
                onClick = { }
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
            PostsScreenContent(
//                allPostsState = allPostsState,
                uiState = uiState,
                postsState = postsState,
                searchTextState = searchTextState,
                isSearchingState = isSearchingState,
                onPullRefresh = viewModel::sync,
//                orderState = orderState,
//                windowInfo = windowInfo,
//                onPostCardClick = onPostCardClick,
//                debug = debug,
                onPostLongClick = { postId: Int ->
                    viewModel.toggleSelectedPost(postId)
                },
//                postsSearchBar = {
//                    PostsSearchBar { it: String ->
//                        viewModel.onEvent(PostListingsEvent.OnSearchQueryChange(it))
//                    }
//                },
//                sortComp = {
//                    SortComp(
//                        isOrderSectionVisible = isOrderSectionVisible,
//                        orderState = orderState,
//                        onOrderChange = { it: PostOrder ->
//                            viewModel.onEvent(PostListingsEvent.Order(it))
//                        },
//                        onToggleVisibilityOfSortBarButtonClick = {
//                            viewModel.onEvent(
//                                PostListingsEvent.ToggleOrderSection
//                            )
//                        }
//                    )
//                },
//                onFollowPostClick = { postId: Int ->
//                    viewModel.onEvent(PostListingsEvent.ToggleFollowedPost(postId))
//                }
            )
        }
    }

}

//@Composable
//private fun SortComp(
//    isOrderSectionVisible: Boolean,
//    onToggleVisibilityOfSortBarButtonClick: () -> Unit,
//    orderState: OrderState,
//    onOrderChange: (PostOrder) -> Unit,
//) {
//
//    ToggleVisibilityOfSortBarButton(
//        isOrderSectionVisible = isOrderSectionVisible,
//        onClick = onToggleVisibilityOfSortBarButtonClick
//    )
//
//    PostsSortBar(
//        isOrderSectionVisible = isOrderSectionVisible,
//        orderState = orderState,
//        showCompactRow = false,
//        onOrderChange = onOrderChange
//    )
//
//}

@Composable
internal fun PostsScreenContent(
//    windowInfo: WindowInfo,
//    onPostCardClick: (Post) -> Unit,
//    allPostsState: PostListingsState,
    postsState: PostListingsState,
    isSearchingState: Boolean,
    searchTextState: String,
//    orderState: OrderState,
    onPullRefresh: () -> Unit,
    sortComp: @Composable() (() -> Unit)? = null,
//    onFollowPostClick: (Int) -> Unit,
    postsSearchBar: @Composable() (() -> Unit)? = null,
    uiState: PostListingsState,
    onPostLongClick: (Int) -> Unit,
//    debug: PostListingsState,
) {

    Column(modifier = Modifier.fillMaxHeight()) {
        postsSearchBar?.invoke()
        sortComp?.invoke()

        PostsListViewerComp(
            postsState = postsState,
//                    onPostCardClick,
            searchTextState = searchTextState,
            isSearchingState = isSearchingState,
            uiState = uiState,
//            debug = debug,
//            onFollowPostClick = onFollowPostClick,
            onPullRefresh = onPullRefresh,
            onPostLongClick = onPostLongClick
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
private fun PostsScreenPreview() {
    MaterialTheme {
//        PostsScreen()
    }
}