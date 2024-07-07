package com.notifgram.presentation_channel.order

/***
 * Used for sorting
 */
sealed class ChannelOrder(val orderType: OrderType) {
    class Name(orderType: OrderType) : ChannelOrder(orderType)
    class IsFollowed(orderType: OrderType) : ChannelOrder(orderType)

    fun copy(orderType: OrderType): ChannelOrder {
        return when (this) {
            is Name -> Name(orderType)
            is IsFollowed -> IsFollowed(orderType)
        }
    }
}