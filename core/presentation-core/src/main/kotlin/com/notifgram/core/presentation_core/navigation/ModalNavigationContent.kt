package com.notifgram.core.presentation_core.navigation

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.automirrored.outlined.Message
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.BugReport
import androidx.compose.material.icons.outlined.Cloud
import androidx.compose.material.icons.outlined.Dataset
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch

@Composable
fun ModalNavigationContent(
    navController: NavController,
    //coroutineScope: CoroutineScope, //me: is needed as a paramter?
    //drawerState: DrawerState,
    closeDrawer: () -> Unit
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute =
        navBackStackEntry?.destination?.route ?: Screen.MainScreen.route

    Column(Modifier.verticalScroll(rememberScrollState())) {

        NavigationDrawerItem(
            icon = { Icon(Icons.Outlined.Home, contentDescription = null) },
            label = { Text(Screen.MainScreen.title) },
            selected = currentRoute == Screen.MainScreen.route,
            onClick = {
                openHomeScreen(navController)
                closeDrawer()
            }
        )

        /////////       FOR CHANNELS      /////////
        NavigationDrawerItem(
            icon = { Icon(Icons.AutoMirrored.Outlined.List, contentDescription = null) },
            label = { Text(Screen.ChannelsScreen.title) },
            selected = currentRoute == Screen.ChannelsScreen.route,
            onClick = {
                openChannelsScreen(navController)
                closeDrawer()
            }
        )

        ///////////////////     FOR POSTS      ///////////////
        NavigationDrawerItem(
            icon = { Icon(Icons.AutoMirrored.Outlined.Message, contentDescription = null) },
            label = { Text(Screen.PostsScreen.title) },
            selected = currentRoute == Screen.PostsScreen.route,
            onClick = {
                openPostsScreen(navController)
                closeDrawer()
            },
        )
        ///////////////////     FOR FCM      ///////////////
        NavigationDrawerItem(
            icon = { Icon(Icons.AutoMirrored.Outlined.Message, contentDescription = null) },
            label = { Text(Screen.FcmScreen.title) },
            selected = currentRoute == Screen.FcmScreen.route,
            onClick = {
                openFcmScreen(navController)
                closeDrawer()
            },
        )

        /////////       FOR DEBUG      /////////
        NavigationDrawerItem(
            icon = { Icon(Icons.Outlined.BugReport, contentDescription = null) },
            label = { Text(Screen.DebugScreen.title) },
            selected = currentRoute == Screen.DebugScreen.route,
            onClick = {
                openDebugScreen(navController)
                closeDrawer()
            }
        )

        /////////       FOR LOCAL GENERATOR       /////////
        NavigationDrawerItem(
            icon = { Icon(Icons.Outlined.Dataset, contentDescription = null) },
            label = { Text(Screen.LocalSeedDataScreen.title) },
            selected = currentRoute == Screen.LocalSeedDataScreen.route,
            onClick = {
                openLocalSeedDataScreen(navController)
                closeDrawer()
            },
        )

        /////////       FOR REMOTE GENERATOR       /////////
        NavigationDrawerItem(
            icon = { Icon(Icons.Outlined.Cloud, contentDescription = null) },
            label = { Text(Screen.RemoteSeedDataScreen.title) },
            selected = currentRoute == Screen.RemoteSeedDataScreen.route,
            onClick = {
                openRemoteSeedDataScreen(navController)
                closeDrawer()
            },
        )

        /////////       FOR SETTINGS       /////////
        NavigationDrawerItem(
            icon = {
                if (currentRoute == Screen.SettingsScreen.route)
                    Icon(Icons.Filled.Settings, contentDescription = null)
                else
                    Icon(Icons.Outlined.Settings, contentDescription = null)
            },
            label = { Text(Screen.SettingsScreen.title) },
            selected = currentRoute == Screen.SettingsScreen.route,
            onClick = {
                openSettingsScreen(navController)
                closeDrawer()
            }
        )


    }// for Column

}// End fun

@Preview(name = "dark theme", group = "themes", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview("default")
@Composable
@InternalCoroutinesApi
@ExperimentalCoroutinesApi
private fun ModalNavigationContentPreview() {
    MaterialTheme {
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val coroutineScope = rememberCoroutineScope()
        Surface {
            Column {
                ModalNavigationContent(
                    navController = rememberNavController(),
                    closeDrawer = {
                        if (drawerState.isOpen)
                            coroutineScope.launch { drawerState.close() }
                    }
                )
            }
        }
    }
}