package com.notifgram.presentation_channel.order

import android.content.res.Configuration
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.tooling.preview.Preview
import com.notifgram.core.common.MyLog.i

/**
 * TODO: add to commons
 * Is a toggle Button to Sort list in Ascending or Descending order.
 */
@Composable
internal fun AscDesToggleButton(
    isAscending: Boolean,
    onOrderChange: () -> Unit
) {
    IconButton(
        onClick = {
            onOrderChange()
        }
    ) {
        i("AscDesToggleButton")
        Icon(
            modifier = if (isAscending) Modifier.rotate(180F)
            else Modifier,
            imageVector = Icons.AutoMirrored.Filled.Sort,
            contentDescription = "Localized description",
        )
    }
}

@Preview("dark theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview("light theme", showBackground = true)
@Composable
private fun AscDesToggleButtonPreview() {
    MaterialTheme {
//        AscDesToggleButton(onOrderChange = {})
    }
}