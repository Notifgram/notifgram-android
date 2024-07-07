package com.notifgram.presentation.navigation

import androidx.annotation.Keep
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.notifgram.core.presentation_core.navigation.Screen
import com.notifgram.core.presentation_core.utils.WindowInfo
import com.notifgram.presentation_channel.ChannelsScreen
import com.notifgram.presentation_debug.local.LocalSeedDataScreen
import com.notifgram.presentation_debug.remote.RemoteSeedDataScreen
import com.notifgram.presentation_home.HomeScreen
import com.notifgram.presentation_post.PostsScreen
import com.notifgram.presentation_settings.SettingsScreen
import com.notifgram.fcm.CachedFilesScreen
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi

@Keep
@Composable
@InternalCoroutinesApi
@ExperimentalCoroutinesApi
fun SetupNavigationHost(
    navController: NavHostController,
    modifier: Modifier,
    windowInfo: WindowInfo
) {

    NavHost(
        navController = navController,
        startDestination = Screen.PostsScreen.route,
        modifier = modifier
    )
    {
        composable(
            Screen.MainScreen.route,
            deepLinks = listOf(navDeepLink { uriPattern = Screen.MainScreen.deepLinkRoute })
        ) {
            HomeScreen()
        }

        ////////////////////////////////////////////////////////////////////////////////
        //////////////////////           CHANNELS         //////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////
        composable(
            Screen.ChannelsScreen.route,
            deepLinks = listOf(navDeepLink { uriPattern = Screen.ChannelsScreen.deepLinkRoute })
        ) {
            ChannelsScreen(
                windowInfo = windowInfo,
                navController = navController
            )
        }

        ////////////////////////////////////////////////////////////////////////////////
        /////////////////////////           POSTS         //////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////
        composable(
            Screen.PostsScreen.route,
            deepLinks = listOf(navDeepLink { uriPattern = Screen.PostsScreen.deepLinkRoute })
        ) {
            PostsScreen(
                windowInfo = windowInfo,
                navController = navController
            )
        }

        ////////////////////////////////////////////////////////////////////////////////
        //////////////////////////           FCM         ///////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////
        composable(
            Screen.FcmScreen.route,
            deepLinks = listOf(navDeepLink {
                uriPattern = Screen.FcmScreen.deepLinkRoute
            })
        ) {
            CachedFilesScreen()
        }


        ////////////////////////////////////////////////////////////////////////////////
        //////////////////////////           DATA GENERATORS         ///////////////////
        ////////////////////////////////////////////////////////////////////////////////
        composable(
            Screen.LocalSeedDataScreen.route,
            deepLinks = listOf(navDeepLink {
                uriPattern = Screen.LocalSeedDataScreen.deepLinkRoute
            })
        ) {
            LocalSeedDataScreen()
        }

        composable(
            Screen.RemoteSeedDataScreen.route,
            deepLinks = listOf(navDeepLink {
                uriPattern = Screen.RemoteSeedDataScreen.deepLinkRoute
            })
        ) {
            RemoteSeedDataScreen()
        }


        composable(
            Screen.SettingsScreen.route,
            deepLinks = listOf(navDeepLink { uriPattern = Screen.SettingsScreen.deepLinkRoute })
        ) {
            SettingsScreen()
        }


    }
}
