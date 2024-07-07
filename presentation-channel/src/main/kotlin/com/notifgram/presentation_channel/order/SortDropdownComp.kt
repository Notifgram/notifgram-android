@file:JvmName("SortDropdownCompKt")

package com.notifgram.presentation_channel.order

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.SortByAlpha
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.notifgram.core.common.MyLog.i

private const val TAG = "SortDropdownComp"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SortDropdownComp(
    modifier: Modifier = Modifier,
    selectedChannelOrder: ChannelOrder,// = ChannelOrder.FirstName(OrderType.Ascending),
    onOrderChange: (ChannelOrder) -> Unit
) {
    i("$TAG called")

    var expanded by rememberSaveable { mutableStateOf(false) }
    var selectedOptionText by rememberSaveable { mutableStateOf("نام") }

    @Composable
    fun getColor(isTrue: Boolean): Modifier = if (isTrue)
        modifier.background(MaterialTheme.colorScheme.primary)
    else Modifier


    Row(modifier = Modifier.fillMaxWidth()) {
        ExposedDropdownMenuBox(
            modifier = Modifier.width(150.dp),
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
        ) {
            i("$TAG ExposedDropdownMenuBox")

            OutlinedTextField(
                // The `menuAnchor` modifier must be passed to the text field for correctness.
                modifier = Modifier
                    .menuAnchor()
                    .wrapContentWidth(),
                readOnly = true,
                value = selectedOptionText,
                onValueChange = {},
                //label = { Text("Label") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                leadingIcon = {
                    Icon(
                        Icons.Filled.FilterList,
                        contentDescription = null,
                        //tint = MaterialTheme.colorScheme.primary
                    )
                },
                shape = RoundedCornerShape(55.dp),
                colors = ExposedDropdownMenuDefaults.textFieldColors(),
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                i("$TAG ExposedDropdownMenu")
                DropdownMenuItem(
                    text = { RowContent("name", Icons.Filled.SortByAlpha) },
                    onClick = {
                        selectedOptionText = "name"
                        expanded = false
                        onOrderChange(ChannelOrder.Name(selectedChannelOrder.orderType))
                    },
                    modifier = getColor(selectedChannelOrder is ChannelOrder.Name),
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                )
            }

        }

//        val isAscending = selectedChannelOrder.orderType is OrderType.Ascending
//        AscDesToggleButton(isAscending,onOrderChange)


//        DefaultRadioButton(
//            text = "Ascending",
//            selected = selectedChannelOrder.orderType is OrderType.Ascending,
//            onSelect = {
//                onOrderChange(selectedChannelOrder.copy(OrderType.Ascending))
//            }
//        )
//        Spacer(modifier = Modifier.width(8.dp))
//        DefaultRadioButton(
//            text = "Descending",
//            selected = selectedChannelOrder.orderType is OrderType.Descending,
//            onSelect = {
//                onOrderChange(selectedChannelOrder.copy(OrderType.Descending))
//            }
//        )
    }
}



@Composable
private fun RowContent(text: String, imageVector: ImageVector) {
    Row {
        IconComp1(imageVector = imageVector)
        Spacer(modifier = Modifier.width(20.dp))
        Text(text)
    }
}

@Composable
private fun IconComp1(imageVector: ImageVector) {
    Icon(
        imageVector = imageVector,
        contentDescription = "Localized description",
    )
}

@Preview("dark theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview("light theme", showBackground = true)
@Composable
private fun SortDropdownCompPreview() {
    MaterialTheme {
        SortDropdownComp(
            onOrderChange = {},
            selectedChannelOrder = ChannelOrder.Name(OrderType.Descending)
        )
    }
}