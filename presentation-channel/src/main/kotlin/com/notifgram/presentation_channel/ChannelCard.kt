package com.notifgram.presentation_channel

import android.content.res.Configuration
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BroadcastOnPersonal
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.notifgram.core.domain.entity.Channel
import com.notifgram.core.presentation_core.FakeDataGenerator
import com.notifgram.core.presentation_core.theme.notifgramTheme
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi

@Composable
@InternalCoroutinesApi
@ExperimentalCoroutinesApi
fun ChannelCardBuilder(
    channel: Channel,
//    isShort: Boolean,
    onChannelClick: (Channel?) -> Unit,
    isSelected: Boolean = false,
    onChannelLongClick: (Channel?) -> Unit,
    onFollowChannelClick: () -> Unit,
) {
    val colors: CardColors = if (isSelected)
        cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.primary,
        )
    else
        cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.secondary,
        )

    ChannelCardShort(
        channel = channel,
        colors = colors,
        onChannelClick = onChannelClick,
        isSelected = isSelected,
        onChannelLongClick = onChannelLongClick,
        onFollowChannelClick = onFollowChannelClick
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
@InternalCoroutinesApi
@ExperimentalCoroutinesApi
fun ChannelCardShort(
    channel: Channel?,
    colors: CardColors,
    onChannelClick: (Channel?) -> Unit,
    onChannelLongClick: (Channel?) -> Unit,
    isSelected: Boolean = false,
    onFollowChannelClick: () -> Unit,
) {
    ElevatedCard(
        shape = RoundedCornerShape(4.dp),
        colors = colors,
//        enabled = enabled,
//        onClick = { onChannelClick(channel) },
        modifier = Modifier
            .padding(0.dp)
            .fillMaxWidth()
            .clip(CardDefaults.shape)
            .combinedClickable(
                onClick = { onChannelClick(channel) },// navigates to channel details screen
                onLongClick = { onChannelLongClick(channel) }
            ),
    ) {
        ChannelCardContent(
            channel = channel,
            isSelected = isSelected,
            onFollowChannelClick = onFollowChannelClick
        )
    }
}


@Composable
@InternalCoroutinesApi
@ExperimentalCoroutinesApi
fun ChannelCardContent(
    channel: Channel?,
    isSelected: Boolean = false,
    onFollowChannelClick: () -> Unit,
) {

    Row(
        modifier = Modifier.padding(16.dp),
        //.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        channel?.let { channel ->
            Column(
                modifier = Modifier
                    .weight(1f),
//                    .padding(horizontal = 12.dp, vertical = 4.dp),
//                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = channel.name,
                    maxLines = 1,
                    fontSize = 22.sp,
                    color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer
                    else MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyLarge,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 12.dp, bottom = 8.dp), // s:Reply
                )
                Text(
                    text = channel.description,
                    maxLines = 1,
                    fontSize = 22.sp,
                    color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer
                    else MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyLarge,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 12.dp, bottom = 8.dp), // s:Reply
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        if (channel != null) {
            Icon(
                Icons.Filled.BroadcastOnPersonal,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )

            IconButton(
                onClick = { onFollowChannelClick() },
                modifier = Modifier
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                Icon(
                    imageVector = if (channel.isFollowed)
                        Icons.Filled.Star
                    else
                        Icons.Filled.StarBorder,
                    contentDescription = "Favorite",
                    tint = MaterialTheme.colorScheme.outline
                )
            }

            AnimatedContent(targetState = isSelected, label = "avatar") { selected ->
                if (selected)
                    Icon(
                        Icons.Filled.Check,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
            }
        }

    }

}

//////////////////////////////////////////////////////////////////////////
///////////////////////////////// PREVIEW ////////////////////////////////
//////////////////////////////////////////////////////////////////////////


@OptIn(InternalCoroutinesApi::class, ExperimentalCoroutinesApi::class)
@Preview("dark theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview("dark theme", device = Devices.TABLET, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview("light theme", showBackground = true)
@Preview("light theme", device = Devices.TABLET, showBackground = true)
@Composable
private fun ChannelCardBuilderPreview() {
    notifgramTheme {
        Column {
            ChannelCardBuilder(
                FakeDataGenerator().generateSingleSampleChannel("name", "description"),
                onChannelClick = { },
                onChannelLongClick = {},
                isSelected = false,
                onFollowChannelClick = {}
            )
        }
    }
}