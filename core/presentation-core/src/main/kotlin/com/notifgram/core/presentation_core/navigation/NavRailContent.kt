package com.notifgram.core.presentation_core.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.automirrored.outlined.Message
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.BugReport
import androidx.compose.material.icons.outlined.Cloud
import androidx.compose.material.icons.outlined.Dataset
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import kotlinx.coroutines.launch

@Composable
fun NavRailContent(
    navController: NavController,
    drawerState: DrawerState        //Used for opening ModalNavDrawer
) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute =
        navBackStackEntry?.destination?.route ?: Screen.MainScreen.route

    val scope = rememberCoroutineScope()    //Used for opening ModalNavDrawer

    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            verticalArrangement = Arrangement.Top
        ) {
            NavigationRailItem(
                selected = false,
                onClick = {
                    if (drawerState.isClosed)
                        scope.launch { drawerState.open() }
                    else
                        scope.launch { drawerState.close() }
                },
                icon = {
                    Icon(
                        imageVector = Icons.Filled.Menu,
                        contentDescription = "stringResource(id = R.string.navigation_drawer)"
                    )
                }
            )

//            FloatingActionButton(
//                onClick = { /*TODO*/ },
//                modifier = Modifier.padding(top = 8.dp, bottom = 32.dp),
//                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
//                contentColor = MaterialTheme.colorScheme.onTertiaryContainer
//            ) {
//                Icon(
//                    imageVector = Icons.Default.NotificationAdd,
//                    contentDescription = "stringResource(id = R.string.edit)",
//                    modifier = Modifier.size(18.dp)
//                )
//            }

        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(2f)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top
        ) {

            ///////////////////     FOR HOME      ///////////////
            NavigationRailItem(
                icon = { Icon(Icons.Outlined.Home, contentDescription = null) },
                label = { Text(Screen.MainScreen.title) },
                selected = currentRoute == Screen.MainScreen.route,
                onClick = { openHomeScreen(navController) },
                alwaysShowLabel = false
            )

            ///////////////////     FOR CHANNELS      ///////////////
            NavigationRailItem(
                icon = { Icon(Icons.AutoMirrored.Outlined.List, contentDescription = null) },
                label = { Text(Screen.ChannelsScreen.title) },
                selected = currentRoute == Screen.ChannelsScreen.route,
                onClick = { openChannelsScreen(navController) },
                alwaysShowLabel = false
            )
            ///////////////////     FOR POSTS      ///////////////
            NavigationRailItem(
                icon = { Icon(Icons.AutoMirrored.Outlined.Message, contentDescription = null) },
                label = { Text(Screen.PostsScreen.title) },
                selected = currentRoute == Screen.PostsScreen.route,
                onClick = { openPostsScreen(navController) },
                alwaysShowLabel = false
            )
            ///////////////////     FOR FCM      ///////////////
            NavigationRailItem(
                icon = { Icon(Icons.AutoMirrored.Outlined.Message, contentDescription = null) },
                label = { Text(Screen.FcmScreen.title) },
                selected = currentRoute == Screen.FcmScreen.route,
                onClick = { openFcmScreen(navController) },
                alwaysShowLabel = false
            )

            /////////       FOR DEBUG SCREEN     /////////
            NavigationRailItem(
                icon = { Icon(Icons.Outlined.BugReport, contentDescription = null) },
                label = { Text(Screen.DebugScreen.title) },
                selected = currentRoute == Screen.DebugScreen.route,
                onClick = { openDebugScreen(navController) },
                alwaysShowLabel = false
            )

            /////////       FOR LOCAL GENERATOR       /////////
            NavigationRailItem(
                icon = { Icon(Icons.Outlined.Dataset, contentDescription = null) },
                label = { Text(Screen.LocalSeedDataScreen.title) },
                selected = currentRoute == Screen.LocalSeedDataScreen.route,
                onClick = { openLocalSeedDataScreen(navController) },
                //alwaysShowLabel = false
            )

            /////////       FOR REMOTE GENERATOR       /////////
            NavigationRailItem(
                icon = { Icon(Icons.Outlined.Cloud, contentDescription = null) },
                label = { Text(Screen.RemoteSeedDataScreen.title) },
                selected = currentRoute == Screen.RemoteSeedDataScreen.route,
                onClick = { openRemoteSeedDataScreen(navController) },
                //alwaysShowLabel = false
            )


            ///////////        SETTINGS SCREEN      /////////////////////
            NavigationRailItem(
                icon = {
                    if (currentRoute == Screen.SettingsScreen.route)
                        Icon(Icons.Filled.Settings, contentDescription = null)
                    else
                        Icon(Icons.Outlined.Settings, contentDescription = null)
                },
                label = { Text(Screen.SettingsScreen.title) },
                selected = currentRoute == Screen.SettingsScreen.route,
                onClick = { openSettingsScreen(navController) },
                alwaysShowLabel = false
            )


        }
    }

}