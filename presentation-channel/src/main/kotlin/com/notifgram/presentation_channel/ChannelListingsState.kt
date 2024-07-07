package com.notifgram.presentation_channel

import com.notifgram.core.domain.entity.Channel
import com.notifgram.presentation_channel.order.ChannelOrder
import com.notifgram.presentation_channel.order.OrderType


data class ChannelListingsState(
    val items: List<Channel> = emptyList(),
    val searchText: String = "",
    val isLoading: Boolean = false,
    val channelOrder: ChannelOrder = ChannelOrder.Name(OrderType.Ascending),
    val error: String? = null,
    val selectedChannels: Set<Int> = emptySet(),
//    val isOrderSectionVisible: Boolean = false
    //val isRefreshing: Boolean = false,
)
