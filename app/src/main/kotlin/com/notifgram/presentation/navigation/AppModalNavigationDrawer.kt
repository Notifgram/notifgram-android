package com.notifgram.presentation.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.notifgram.core.presentation_core.navigation.ModalNavigationContent
import com.notifgram.presentation.AppScaffold
import com.notifgram.presentation.BackPressHandler
import kotlinx.coroutines.launch


@Composable
fun AppModalNavigationDrawer() {
    val navController = rememberNavController()

    //////////// BEGIN Used for ModalNavigationDrawer////////////////////
    val coroutineScope = rememberCoroutineScope()
    val drawerState: DrawerState =
        rememberDrawerState(initialValue = DrawerValue.Closed)
    // Intercepts back navigation when the drawer is open
    if (drawerState.isOpen) {
        BackPressHandler {
            coroutineScope.launch {
                drawerState.close()
            }
        }
    }
    //////////// END Used for ModalNavigationDrawer////////////////////

    val closeDrawer = {
        if (drawerState.isOpen)
            coroutineScope.launch { drawerState.close() }
    }



    ModalNavigationDrawer(
        //modifier = modifier,
        drawerState = drawerState,
        gesturesEnabled = drawerState.isOpen,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(Modifier.height(12.dp))

                Column {
                    ModalNavigationContent(
                        navController = navController,
                        closeDrawer = closeDrawer
                    )
                }
            }
        },
        content = {
            AppScaffold(
                drawerState = drawerState,
                navController = navController
            )


        } //End content
    )// End ModalNavigationDrawer
}//End AppModalNavigationDrawer
