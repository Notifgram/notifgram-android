package com.notifgram.presentation_channel.order

sealed class OrderType {
    data object Ascending : OrderType()
    data object Descending : OrderType()
}
