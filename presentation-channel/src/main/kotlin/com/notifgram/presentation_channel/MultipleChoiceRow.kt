package com.notifgram.presentation_channel

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SortByAlpha
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.notifgram.presentation_channel.order.ChannelOrder
import com.notifgram.presentation_channel.order.OrderType

//TODO: put this in core
class MultipleChoiceRowItem(
    val text: String,
    val channelOrder: ChannelOrder,
    val iconImageVector: ImageVector?
)

/**
 * TODO: put this in core
 * Shows list of [items] from which only one is selectable.
 */
@Composable
fun MultipleChoiceRow(
    items: List<MultipleChoiceRowItem>,
    selectedItem: MultipleChoiceRowItem,
    onItemClicked: (MultipleChoiceRowItem) -> Unit
) {

    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {

        items.forEach {
            ElevatedFilterChip(
                selected = it == selectedItem,
                shape = CircleShape,
                onClick = {
                    if (it != selectedItem)
                        onItemClicked(it)
                },
                label = {
//                    Row(
//                        horizontalArrangement = Arrangement.Center,
//                        modifier = Modifier.wrapContentWidth(
//                            Alignment.CenterHorizontally
//                        )
//                    ) {
                    Text(
                        it.text, modifier = Modifier
//                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.CenterHorizontally)
                    )
//                    }
                },
                modifier = Modifier
                    .padding(10.dp, 5.dp)
                    .weight(1f)
                    .wrapContentWidth(align = Alignment.CenterHorizontally),
//                    .fillMaxWidth(),
                leadingIcon = {
                    it.iconImageVector?.let { it1 ->
                        Icon(
                            imageVector = it1,
                            contentDescription = "Localized description",
                            Modifier.size(AssistChipDefaults.IconSize)
                        )
                    }
                }

            )
        } // end items.forEach

    } // end row
}


@Preview(name = "dark theme", group = "themes", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showBackground = true)
@Composable
private fun SingleChoiceRowPreview() {
    val items = listOf(
        MultipleChoiceRowItem(
            "sampleName",
            iconImageVector = Icons.Filled.SortByAlpha,
            channelOrder = ChannelOrder.Name(OrderType.Descending),
        ),

    )

    MaterialTheme {
        MultipleChoiceRow(items, items[0]) {}
    }
}
