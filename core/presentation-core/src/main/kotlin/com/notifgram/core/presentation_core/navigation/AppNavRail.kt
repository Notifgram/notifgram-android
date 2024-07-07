package com.notifgram.core.presentation_core.navigation

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavRail(
    navController: NavController,
    modifier: Modifier = Modifier,
    drawerState: DrawerState        //Used for opening ModalNavDrawer
) {

    NavigationRail(
        header = {
//            Icon(
//                painterResource(R.),
//                null,
//                Modifier.padding(vertical = 12.dp),
//                tint = MaterialTheme.colorScheme.primary
//            )
        },
        modifier = modifier
            .fillMaxHeight()
            .widthIn(max = 80.dp) //fillMaxHeight is not needed
    ) {
        NavRailContent(navController, drawerState = drawerState)
    }
}

@Preview("Drawer contents (dark)", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview("Drawer contents")
@Composable
fun AppNavRailPreview() {
    val drawerState: DrawerState =
        rememberDrawerState(initialValue = DrawerValue.Closed)
    val navController =
        rememberNavController()
    MaterialTheme {
        AppNavRail(drawerState = drawerState, navController = navController)
    }
}
