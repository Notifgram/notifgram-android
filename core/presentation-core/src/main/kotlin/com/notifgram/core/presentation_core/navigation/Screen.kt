package com.notifgram.core.presentation_core.navigation

sealed class Screen(val route: String, val deepLinkRoute: String, val title: String) {

    data object MainScreen :
        Screen("MainScreen", DeeplinkUri.DEEP_LINK_URI.plus("MainScreen"), "MainScreen")

    ////////////////////////////////////////////////////////////////////////////////
    //////////////////////////           CHANNELS LIST         ///////////////////
    ////////////////////////////////////////////////////////////////////////////////

    data object ChannelsScreen :
        Screen("ChannelsScreen", DeeplinkUri.DEEP_LINK_URI.plus("ChannelsScreen"), "Channels")

    ////////////////////////////////////////////////////////////////////////////////
    //////////////////////////           POSTS LIST         ////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    data object PostsScreen :
        Screen("PostsScreen", DeeplinkUri.DEEP_LINK_URI.plus("PostsScreen"), "PostsScreen")

    ////////////////////////////////////////////////////////////////////////////////
    //////////////////////////           FCM         ///////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    data object FcmScreen :
        Screen("FcmScreen", DeeplinkUri.DEEP_LINK_URI.plus("FcmScreen"), "FCM")

    ////////////////////////////////////////////////////////////////////////////////
    //////////////////////////           DATA GENERATORS         ///////////////////
    ////////////////////////////////////////////////////////////////////////////////
    data object LocalSeedDataScreen :
        Screen(
            "LocalSeedDataScreen",
            DeeplinkUri.DEEP_LINK_URI.plus("LocalSeedDataScreen"),
            "Local Seed Data"
        )

    data object RemoteSeedDataScreen :
        Screen(
            "RemoteSeedDataScreen",
            DeeplinkUri.DEEP_LINK_URI.plus("RemoteSeedDataScreen"),
            "Remote Seed Data"
        )

    data object SettingsScreen :
        Screen("SettingsScreen", DeeplinkUri.DEEP_LINK_URI.plus(""), "Settings")

    ////////////////////////////////////////////////////////////////////////////////
    //////////////////////////           DEBUG SCREEN          ///////////////////
    ////////////////////////////////////////////////////////////////////////////////
    data object DebugScreen :
        Screen(
            "DebugScreen",
            DeeplinkUri.DEEP_LINK_URI.plus("DebugScreen"),
            "Debug"
        )
}

//For deeplinking
object DeeplinkUri {
    const val DEEP_LINK_URI = "x://www.a.com/"
}