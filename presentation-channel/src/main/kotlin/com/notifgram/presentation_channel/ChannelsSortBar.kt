package com.notifgram.presentation_channel

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.SortByAlpha
import androidx.compose.runtime.Composable
import com.notifgram.core.common.MyLog.i
import com.notifgram.presentation_channel.order.AscDesToggleButton
import com.notifgram.presentation_channel.order.ChannelOrder
import com.notifgram.presentation_channel.order.OrderType
import com.notifgram.presentation_channel.order.SortDropdownComp

private const val TAG = "ChannelsSortBar"

/**
 * Used by ChannelsColumnOnMainScreen and ChannelsScreen
 */
@Composable
fun ChannelsSortBar(
    orderState: OrderState,
    isOrderSectionVisible: Boolean,
    showCompactRow: Boolean = false,
    onOrderChange: (ChannelOrder) -> Unit,
) {
    i("$TAG showCompactRow=$showCompactRow")
    AnimatedVisibility(
        visible = isOrderSectionVisible,
        enter = fadeIn() + slideInVertically(),
        exit = fadeOut() + slideOutVertically()
    ) {
        val isAscending = orderState.channelOrder.orderType is OrderType.Ascending
        if (showCompactRow)
            Row {
                AscDesToggleButton(isAscending) {
                    if (isAscending)
                        onOrderChange(orderState.channelOrder.copy(OrderType.Descending))
                    else
                        onOrderChange(orderState.channelOrder.copy(OrderType.Ascending))
                }
                SortDropdownComp(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(vertical = 16.dp),
                    selectedChannelOrder = orderState.channelOrder,
                    onOrderChange = onOrderChange
                )
            }
        else {
            val items = listOf(
                MultipleChoiceRowItem(
                    "name",
                    iconImageVector = Icons.Filled.SortByAlpha,
                    channelOrder = ChannelOrder.Name(orderState.channelOrder.orderType)
                ),
                MultipleChoiceRowItem(
                    "Followed",
                    iconImageVector = Icons.Filled.Favorite,
                    channelOrder = ChannelOrder.IsFollowed(orderState.channelOrder.orderType)
                ),
            )


            Row {
                AscDesToggleButton(isAscending) {
                    if (isAscending)
                        onOrderChange(orderState.channelOrder.copy(OrderType.Descending))
                    else
                        onOrderChange(orderState.channelOrder.copy(OrderType.Ascending))
                }
                i("orderState.channelOrder=${orderState.channelOrder}")
                MultipleChoiceRow(
                    items,
                    selectedItem = items.single { it.channelOrder::class == orderState.channelOrder::class }
                ) { item: MultipleChoiceRowItem ->
                    onOrderChange(item.channelOrder)
                }
            }

        }

    }

}
