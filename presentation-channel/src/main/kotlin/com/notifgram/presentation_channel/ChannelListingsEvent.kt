package com.notifgram.presentation_channel

import com.notifgram.presentation_channel.order.ChannelOrder


sealed class ChannelListingsEvent {
    data object Refresh : ChannelListingsEvent()
    data class OnSearchQueryChange(val query: String) :
        ChannelListingsEvent() //query is not needed for now

    data class ToggleFollowedChannel(val id: Int, val newValue: Boolean) : ChannelListingsEvent()
    data object ToggleOrderSection : ChannelListingsEvent()
    data class Order(val channelOrder: ChannelOrder) : ChannelListingsEvent()
}
