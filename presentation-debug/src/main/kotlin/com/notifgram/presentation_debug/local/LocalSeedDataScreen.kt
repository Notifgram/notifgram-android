package com.notifgram.presentation_debug.local

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.notifgram.core.presentation_core.theme.NotifgramTheme
import com.notifgram.core.presentation_core.utils.DevicePreviews
import com.notifgram.presentation_debug.SeedDataState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch

@Composable
fun LocalSeedDataScreen(
    viewModel: LocalSeedDataViewModel = hiltViewModel(),
) {
    val state = viewModel.state

    val coroutineScope = rememberCoroutineScope()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {

        ////////////////////////////////////////////////////////////////////////////////
        //////////////////////////           CHANNELS               //////////////////
        ////////////////////////////////////////////////////////////////////////////////

        Button(onClick = {
            coroutineScope.launch {
                viewModel.generateSampleChannels()
            }
        }
        ) {
            Text("Generate sample local Channels directly to room")
        }

        Button(colors = ButtonDefaults.buttonColors(containerColor = Color.Red), onClick = {
            coroutineScope.launch {
                viewModel.deleteAllLocalSampleChannels()
            }
        }
        ) {
            Text("delete all local Channels directly from room")
        }
        Button(colors = ButtonDefaults.buttonColors(containerColor = Color.Green), onClick = {
            coroutineScope.launch {
                viewModel.loadAllLocalChannels()
            }
        }
        ) {
            Text("LOAD all local Channels directly from room")
        }
        HorizontalDivider(modifier = Modifier.height(10.dp))

        ////////////////////////////////////////////////////////////////////////////////
        //////////////////////////           POSTS               //////////////////
        ////////////////////////////////////////////////////////////////////////////////

        Button(onClick = {
            coroutineScope.launch {
                viewModel.generateSamplePosts()
            }
        }
        ) {
            Text("Generate sample local Posts directly to room")
        }

        Button(colors = ButtonDefaults.buttonColors(containerColor = Color.Red), onClick = {
            coroutineScope.launch {
                viewModel.deleteAllLocalSamplePosts()
            }
        }
        ) {
            Text("delete all local Posts directly from room")
        }
        Button(colors = ButtonDefaults.buttonColors(containerColor = Color.Green), onClick = {
            coroutineScope.launch {
                viewModel.loadAllLocalPosts()
            }
        }
        ) {
            Text("LOAD all local Posts directly from room")
        }
        HorizontalDivider(modifier = Modifier.height(10.dp))
        ////////////////////////////////////////////////////////////////////////////////
        //////////////////////////           LOGGER               //////////////////
        ////////////////////////////////////////////////////////////////////////////////
        Button(colors = ButtonDefaults.buttonColors(containerColor = Color.Red), onClick = {
            viewModel.state = SeedDataState()
        }
        ) {
            Text("clear log")
        }

        Text(
            text = "channels count= ${state.channelsList.count()}",
            modifier = Modifier.padding(15.dp),
            color = Color.Green
        )

        state.channelsList.forEach {
            Text(text = it.toString(), modifier = Modifier.padding(15.dp))
            HorizontalDivider(modifier = Modifier.height(3.dp))
        }

        HorizontalDivider(modifier = Modifier.height(10.dp))

        Text(
            text = "posts count= ${state.postsList.count()}",
            modifier = Modifier.padding(15.dp),
            color = Color.Green
        )

        state.postsList.forEach {
            Text(text = it.toString(), modifier = Modifier.padding(15.dp))
            HorizontalDivider(modifier = Modifier.height(3.dp))
        }

    }

}


@DevicePreviews
@Composable
@InternalCoroutinesApi
@ExperimentalCoroutinesApi
private fun LocalSeedDataScreenPreview() {
    NotifgramTheme {
        LocalSeedDataScreen()
    }
}
