package com.notifgram.presentation_debug.remote

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.notifgram.presentation_debug.SeedDataState
import kotlinx.coroutines.launch

@Composable
fun RemoteSeedDataScreen(
    viewModel: RemoteSeedDataViewModel = hiltViewModel(),
) {
    val state = viewModel.state
    if (state.isLoading) LinearProgressIndicator(modifier = Modifier.fillMaxWidth())

    val coroutineScope = rememberCoroutineScope()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {

        ////////////////////////////////////////////////////////////////////////////////
        ////////////////////////           CHANNELS               //////////////////
        ////////////////////////////////////////////////////////////////////////////////
        HorizontalDivider(modifier = Modifier.height(10.dp))

        Button(colors = ButtonDefaults.buttonColors(containerColor = Color.Green), onClick = {
            coroutineScope.launch {
                viewModel.getChannels()
            }
        }
        ) {
            Text("LOAD all Channels from backend")
        }

        Button(onClick = {
            coroutineScope.launch {
                viewModel.generateSampleChannel()
            }
        }
        ) {
            Text("Generate sample single remote Channel ")
        }

        ////////////////////////////////////////////////////////////////////////////////
        ////////////////////////           POSTS               //////////////////
        ////////////////////////////////////////////////////////////////////////////////
        HorizontalDivider(modifier = Modifier.height(10.dp))

        Button(colors = ButtonDefaults.buttonColors(containerColor = Color.Green), onClick = {
            coroutineScope.launch {
                viewModel.getPosts()
            }
        }
        ) {
            Text("LOAD all Posts from backend")
        }

        Button(onClick = {
            coroutineScope.launch {
                viewModel.generateSamplePost()
            }
        }
        ) {
            Text("Generate sample single remote Post ")
        }

        ////////////////////////////////////////////////////////////////////////////////
        //////////////////////////           LOGGER               //////////////////
        ////////////////////////////////////////////////////////////////////////////////
        Text("Response: ${viewModel.response}")
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

    } // end column

}