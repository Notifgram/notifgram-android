package com.notifgram.presentation_channel

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.notifgram.core.common.MyLog.i
import com.notifgram.core.presentation_core.components.SearchBarComp
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi

@Composable
fun ChannelsSearchBar(onValueChange: (String) -> Unit) {
    i("ChannelsSearchBar")
    SearchBarComp(
        leadingIcon = Icons.Filled.Search,
        placeholderText = "Search in channels",
        onValueChange = onValueChange
    )
}


@Preview("dark theme")
@Preview("light theme", showBackground = true)
@Composable
@InternalCoroutinesApi
@ExperimentalCoroutinesApi
fun ChannelsSearchBarPreview() {
    MaterialTheme {
        ChannelsSearchBar(onValueChange = {})
    }
}
