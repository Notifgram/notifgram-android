package com.notifgram.core.presentation_core.navigation

import androidx.navigation.NavController

val HOME_SCREEN = Screen.MainScreen.route

fun openHomeScreen(navController: NavController) {
    navController.navigate(Screen.MainScreen.route) {
        popUpTo(HOME_SCREEN)

        launchSingleTop = true
        restoreState = true // I don't know is working or not!
    }
}

fun openChannelsScreen(navController: NavController) {
    navController.navigate(Screen.ChannelsScreen.route) {
        popUpTo(HOME_SCREEN)
        launchSingleTop = true
        restoreState = true
    }
}

fun openPostsScreen(navController: NavController) {
    navController.navigate(Screen.PostsScreen.route) {
        popUpTo(HOME_SCREEN)
        launchSingleTop = true
        restoreState = true
    }
}

fun openFcmScreen(navController: NavController) {
    navController.navigate(Screen.FcmScreen.route) {
        popUpTo(HOME_SCREEN)
        launchSingleTop = true
        restoreState = true
    }
}

fun openSettingsScreen(navController: NavController) {
    navController.navigate(Screen.SettingsScreen.route) {
        popUpTo(HOME_SCREEN)
        launchSingleTop = true
        restoreState = true
    }
}

fun openDebugScreen(navController: NavController) {
    navController.navigate(Screen.DebugScreen.route) {
        popUpTo(HOME_SCREEN)
        launchSingleTop = true
        restoreState = true
    }
}

fun openLocalSeedDataScreen(navController: NavController) {
    navController.navigate(Screen.LocalSeedDataScreen.route) {
        popUpTo(HOME_SCREEN)
        launchSingleTop = true
        restoreState = true
    }
}

fun openRemoteSeedDataScreen(navController: NavController) {
    navController.navigate(Screen.RemoteSeedDataScreen.route) {
        popUpTo(HOME_SCREEN)
        launchSingleTop = true
        restoreState = true
    }
}