package com.notifgram.presentation_debug

import com.notifgram.core.domain.entity.Channel
import com.notifgram.core.domain.entity.Post

data class SeedDataState(
    var channelsList: List<Channel> = emptyList(),
    var postsList: List<Post> = emptyList(),
    val isLoading: Boolean = false,
)
