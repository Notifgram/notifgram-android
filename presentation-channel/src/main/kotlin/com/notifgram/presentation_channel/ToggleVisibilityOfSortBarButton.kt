package com.notifgram.presentation_channel

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import com.notifgram.core.common.MyLog.i

/**
 * TODO: put this in common
 * Icon Button that toggles visibility of sort bar
 */
@Composable
internal fun ToggleVisibilityOfSortBarButton(isOrderSectionVisible: Boolean, onClick: () -> Unit) {
    i("ToggleSortBarButton() isOrderSectionVisible=$isOrderSectionVisible")
    IconButton(onClick) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.Sort,
            contentDescription = "Sort"
        )
        if (isOrderSectionVisible)
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "hide Sort bar"
            )
    }
}