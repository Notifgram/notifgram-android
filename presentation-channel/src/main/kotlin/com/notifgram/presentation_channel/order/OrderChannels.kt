package com.notifgram.presentation_channel.order

import com.notifgram.core.common.MyLog.i
import com.notifgram.core.domain.entity.Channel
import com.notifgram.presentation_channel.OrderState

/**
 * Sorts [list] based on [orderState]
 * is used in ChannelsViewModel.
 */
internal fun orderChannels(list: List<Channel>, orderState: OrderState): List<Channel> {
    i(
        "orderChannels() list=${list.size} " +
                "orderState.channelOrder.orderType=${orderState.channelOrder.orderType}" +
                "orderState.channelOrder=${orderState.channelOrder}"
    )
    return when (orderState.channelOrder.orderType) {
        is OrderType.Ascending -> {
            when (orderState.channelOrder) {
                is ChannelOrder.Name -> list.sortedBy { it.name }
                is ChannelOrder.IsFollowed -> list.sortedBy { it.isFollowed }
            }
        }

        is OrderType.Descending -> {
            when (orderState.channelOrder) {
                is ChannelOrder.Name -> list.sortedByDescending { it.name }
                is ChannelOrder.IsFollowed -> list.sortedByDescending { it.isFollowed }
            }
        }


    }
}