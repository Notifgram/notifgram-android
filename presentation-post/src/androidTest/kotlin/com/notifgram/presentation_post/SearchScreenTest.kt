package com.notifgram.presentation_post

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PostsScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun shows_not_found_composable_on_empty_results_test() {
        composeTestRule.setContent {
            PostsScreenContent(
                postsState = PostListingsState(items = emptyList()),
                isSearchingState = false,
                searchTextState = "",
                uiState = PostListingsState(),
//                searchResultUiState = SearchResultUiState.Success(),
//            navController=rememberNavController(),
//                windowInfo = rememberWindowInfo(),
                onPostLongClick = {},
                onPullRefresh = {}
            )
        }

        composeTestRule
            .onNodeWithText("No items found")
            .assertIsDisplayed()
    }
}