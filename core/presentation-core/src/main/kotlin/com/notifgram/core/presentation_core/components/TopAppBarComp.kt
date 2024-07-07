package com.notifgram.core.presentation_core.components

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
@InternalCoroutinesApi
@ExperimentalCoroutinesApi
fun ShowTopAppBar(
    navController: NavController,
    //scaffoldState: ScaffoldState
    drawerState: DrawerState,
) {

    val scope = rememberCoroutineScope()
    TopAppBar(
        title = { Text("notifgram") },
        navigationIcon = {
            IconButton(onClick = { /* doSomething() */ }) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "Localized description",
                    modifier = Modifier.clickable(onClick = {
                        //scaffoldState.drawerState.open()
                        if (drawerState.isClosed)
                            scope.launch { drawerState.open() }
                        else
                            scope.launch { drawerState.close() }
                    })
                )
            }
        },
        actions = {

//            IconButton(onClick = {
//            }) {
//                Icon(
//                    imageVector = Icons.Filled.Favorite,
//                    contentDescription = "Localized description"
//                )
//            }
//
//            IconButton(onClick = { })
//            {
//                BadgedBox(badge = { Badge { Text("8") } }) {
//                    Icon(
//                        Icons.Filled.Notifications,
//                        contentDescription = "Favorite"
//                    )
//                }
//            }


        }
    )
}

@OptIn(InternalCoroutinesApi::class, ExperimentalCoroutinesApi::class)
@Preview(name = "dark theme", group = "themes", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview("default")
@Composable
private fun ShowTopAppBarPreview() {
    val scaffoldState = rememberDrawerState(DrawerValue.Closed)
    MaterialTheme {
        ShowTopAppBar(
            drawerState = scaffoldState,
            navController = rememberNavController(),
        )
    }
}