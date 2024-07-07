package com.notifgram.presentation_debug.local

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.notifgram.core.common.MyLog.i
import com.notifgram.core.domain.entity.Result
import com.notifgram.core.domain.usecase.channel.DeleteAllLocalChannelsUseCase
import com.notifgram.core.domain.usecase.channel.LoadLocalChannelsUseCase
import com.notifgram.core.domain.usecase.debug.local.channel.GenerateSampleChannelsUseCase
import com.notifgram.core.domain.usecase.debug.local.post.GenerateSamplePostsUseCase
import com.notifgram.core.domain.usecase.debug.local.post.LoadLocalPostsFlowUseCase
import com.notifgram.core.domain.usecase.post.DeleteAllLocalPostsUseCase
import com.notifgram.core.presentation_core.FakeDataGenerator
import com.notifgram.presentation_debug.SeedDataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocalSeedDataViewModel @Inject constructor(
    private val generateSampleChannelsUseCase: GenerateSampleChannelsUseCase,
    private val deleteAllLocalChannelsUseCase: DeleteAllLocalChannelsUseCase,
    private val loadLocalChannelsUseCase: LoadLocalChannelsUseCase,
    private val generateSamplePostsUseCase: GenerateSamplePostsUseCase,
    private val deleteAllLocalPostsUseCase: DeleteAllLocalPostsUseCase,
    private val loadLocalPostsFlowUseCase: LoadLocalPostsFlowUseCase,
) : ViewModel() {
    var state by mutableStateOf(SeedDataState())

    fun generateSampleChannels() {
        val channels = FakeDataGenerator().generateSampleChannels(5)
        i("$TAG generateSampleChannels() channels=$channels")
        viewModelScope.launch {
            generateSampleChannelsUseCase.generateSampleChannelsLocally(channels)
        }
    }

    fun deleteAllLocalSampleChannels() {
        i("$TAG deleteAllLocalSampleChannels()")
        viewModelScope.launch {
            deleteAllLocalChannelsUseCase.invoke()
        }
    }

    fun loadAllLocalChannels() {
        i("$TAG loadAllLocalChannels()")
        state = state.copy(channelsList = emptyList())
        viewModelScope.launch {
            loadLocalChannelsUseCase.execute(LoadLocalChannelsUseCase.Request).collect {
                when (it) {
                    is Result.Success -> {
                        state = state.copy(
                            channelsList = it.data.channels
                        )
                    }

                    else -> {}
                }
//                ( it as Result<LoadLocalChannelsUseCase.Response> ).
////                i("loadAllLocalChannels() ${(it as Result. .Response).channels}")
//
            }
        }
    }

    fun generateSamplePosts() {
        val posts = FakeDataGenerator().generateSamplePosts()
        i("$TAG generateSamplePosts() posts=$posts")
        viewModelScope.launch {
            generateSamplePostsUseCase.generateSamplePostsLocally(posts)
        }
    }

    fun deleteAllLocalSamplePosts() {
        i("$TAG deleteAllLocalSamplePosts()")
        viewModelScope.launch {
            deleteAllLocalPostsUseCase.invoke()
        }
    }

    fun loadAllLocalPosts() {
        i("$TAG loadAllLocalPosts()")
        state = state.copy(postsList = emptyList())
        viewModelScope.launch {
            loadLocalPostsFlowUseCase.execute(LoadLocalPostsFlowUseCase.Request).collect {

                when (it) {
                    is Result.Success -> {
                        i("$TAG loadAllLocalPosts() ${it.data.posts}")
                        state = state.copy(
                            postsList = it.data.posts
                        )
                    }

                    else -> {}
                }

            }
        }
    }

    companion object {
        private const val TAG = "LocalSeedDataViewModel"
    }

}