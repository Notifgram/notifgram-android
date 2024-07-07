package com.notifgram.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.notifgram.core.presentation_core.components.ShowTopAppBar
import com.notifgram.core.presentation_core.navigation.AppNavRail
import com.notifgram.core.presentation_core.utils.WindowInfo
import com.notifgram.core.presentation_core.utils.rememberWindowInfo
import com.notifgram.presentation.navigation.SetupNavigationHost
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi

@OptIn(InternalCoroutinesApi::class, ExperimentalCoroutinesApi::class)
@Composable
fun AppScaffold(
    drawerState: DrawerState,
    navController: NavHostController
) {

    val snackbarHostState = remember { SnackbarHostState() }
    val windowInfo = rememberWindowInfo()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            if (windowInfo.screenWidthInfo !is WindowInfo.WindowType.Expanded)
                ShowTopAppBar(
                    drawerState = drawerState,
                    navController = navController
                )

        },

        //////// START FLOATING ACTION BAR //////////
//        floatingActionButton = {
//            if (windowInfo.screenWidthInfo !is WindowInfo.WindowType.Expanded)
//                FloatingActionButton(
//                    elevation = FloatingActionButtonDefaults.elevation(0.dp),
//                    onClick = { /*TODO*/ }
//                ) {
//                    Icon(imageVector = Icons.Default.PlayArrow, contentDescription = "")
//                }
//        },
//        floatingActionButtonPosition = FabPosition.Center
        //////// END FLOATING ACTION BAR //////////

    ) { paddingValues ->

        Row(modifier = Modifier.fillMaxSize()) {

            if (windowInfo.screenWidthInfo is WindowInfo.WindowType.Expanded)
                AppNavRail(navController = navController, drawerState = drawerState)

            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.inverseOnSurface)
            ) {
                SetupNavigationHost(
                    navController = navController,
                    modifier = Modifier,//.weight(1f),// me: seems like is not needed,
                    windowInfo = windowInfo
                )
            }// End Column

        }//End Row

    }// End Scaffold content


}