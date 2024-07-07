package com.notifgram.presentation_debug.remote

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.notifgram.core.common.MyLog.i
import com.notifgram.core.domain.entity.Channel
import com.notifgram.core.domain.entity.Post
import com.notifgram.core.domain.usecase.debug.remote.channel.GetChannelsUseCase
import com.notifgram.core.domain.usecase.debug.remote.channel.PostChannelUseCase
import com.notifgram.core.domain.usecase.debug.remote.post.GetPostsUseCase
import com.notifgram.core.domain.usecase.debug.remote.post.PostPostUseCase
import com.notifgram.core.presentation_core.FakeDataGenerator
import com.notifgram.presentation_debug.SeedDataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RemoteSeedDataViewModel @Inject constructor(
    private val postChannelUseCase: PostChannelUseCase,
    private val getChannelsUseCase: GetChannelsUseCase,
    private val postPostUseCase: PostPostUseCase,
    private val petPostsUseCase: GetPostsUseCase,
) : ViewModel() {
    var state by mutableStateOf(SeedDataState())
    var response by mutableStateOf("")

    ////////////////////////////////////////////////////////////////////////////////
    ////////////////////////           ChannelS               //////////////////
    ////////////////////////////////////////////////////////////////////////////////
    fun generateSampleChannel() {
        val item = FakeDataGenerator().generateSampleChannels(1)
        i("$TAG generateSampleChannel() item=$item")
        viewModelScope.launch {
            postChannelUseCase.postChannel(item[0])
        }
    }

    fun getChannels() {
        i("$TAG getChannels()")
        state = state.copy(channelsList = emptyList())
        viewModelScope.launch {
            getChannelsUseCase.process().collect { it ->
                i("getChannelsUseCase() ${it.channels}")
                var list = mutableListOf<Channel>()
                it.channels.collect { items ->
                    list = items.toMutableList()
                }
                state = state.copy(
                    channelsList = list
                )

            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////
    ////////////////////////           Posts               //////////////////
    ////////////////////////////////////////////////////////////////////////////////
    fun generateSamplePost() {
        val items = FakeDataGenerator().generateSamplePosts()
        i("$TAG generateSamplePost() items=$items")
        viewModelScope.launch {
            postPostUseCase.postPost(items[0])
        }
    }

    fun getPosts() {
        i("$TAG getPosts()")
        state = state.copy(postsList = emptyList())
        viewModelScope.launch {
            petPostsUseCase.process().collect { it ->
                i("petPostsUseCase() ${it.posts}")
                var list = mutableListOf<Post>()
                it.posts.collect { items ->
                    list = items.toMutableList()
                }
                state = state.copy(
                   postsList = list
                )

            }
        }
    }



    companion object {
        private const val TAG = "RemoteSeedDataViewModel"
    }

}