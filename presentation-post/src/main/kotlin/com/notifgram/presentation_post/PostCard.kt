package com.notifgram.presentation_post

import android.content.res.Configuration
import android.graphics.BitmapFactory
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Share
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.notifgram.core.common.MyLog.i
import com.notifgram.core.domain.entity.MediaType
import com.notifgram.core.domain.entity.Post
import com.notifgram.core.presentation_core.FakeDataGenerator
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import java.io.File

@Composable
@InternalCoroutinesApi
@ExperimentalCoroutinesApi
fun PostCardBuilder(
    post: Post,
//    isShort: Boolean,
    onPostClick: (Post?) -> Unit,
    isSelected: Boolean = false,
    onPostLongClick: (Post?) -> Unit,

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

    when (post.mediaType) {
        MediaType.NO_MEDIA -> Unit
        else -> {}
    }

    PostCardShort(
        post = post,
        colors = colors,
        onPostClick = onPostClick,
        isSelected = isSelected,
        onPostLongClick = onPostLongClick,
    )

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
@InternalCoroutinesApi
@ExperimentalCoroutinesApi
fun PostCardShort(
    post: Post?,
    colors: CardColors,
    onPostClick: (Post?) -> Unit,
    onPostLongClick: (Post?) -> Unit,
    isSelected: Boolean = false
) {
    ElevatedCard(
        shape = RoundedCornerShape(4.dp),
        colors = colors,
//        enabled = enabled,
//        onClick = { onPostClick(post) },
        modifier = Modifier
            .padding(0.dp)
            .fillMaxWidth()
            .clip(CardDefaults.shape)
            .combinedClickable(onClick = { onPostClick(post) },// navigates to post details screen
                onLongClick = { onPostLongClick(post) }),
    ) {
        PostCardContent(post = post, isSelected = isSelected)
    }
}


@Composable
@InternalCoroutinesApi
@ExperimentalCoroutinesApi
fun PostCardContent(post: Post?, isSelected: Boolean = false) {

    post?.let { _post ->
        Column {

//            _post.mediaContent ?: i("post.mediaContent=null")
            if (_post.mediaType != MediaType.NO_MEDIA && _post.mediaFile != null)
                if (_post.mediaFile!!.exists())
                MediaViewer(
                    _post.mediaType,
                    _post.mediaFile
                )


            Text(
                text = "text= " + _post.text,
                maxLines = 1,
                fontSize = 22.sp,
                color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer
                else MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyLarge,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(top = 12.dp, bottom = 8.dp), // s:Reply
            )

            Text(
                text = "mediaType= " + _post.mediaType.toString(),
                maxLines = 1,
                fontSize = 22.sp,
                color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer
                else MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyLarge,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(top = 12.dp, bottom = 8.dp), // s:Reply
            )

            Text(
                text = "Uri= " + _post.mediaFile?.path.toString(),
//                maxLines = 1,
                fontSize = 18.sp,
                color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer
                else MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyLarge,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(top = 12.dp, bottom = 8.dp), // s:Reply
            )
        }


        Spacer(modifier = Modifier.width(8.dp))

        Row {

            Icon(
                Icons.Filled.Share,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )

            IconButton(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                Icon(
                    imageVector = Icons.Default.StarBorder,
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

@Composable
fun MediaViewer(
    mediaType: MediaType,
    mediaFile: File?
) {
    i("MediaViewer mediaType=$mediaType , mediaFile=$mediaFile")
    when (mediaType) {
        MediaType.NO_MEDIA -> Unit
        MediaType.IMAGE -> {
            if (mediaFile != null)
                ImageViewer(
                    imageContent = byteArrayToImageBitmap(mediaFile.readBytes())
                )
        }

        MediaType.AUDIO -> if (mediaFile != null) {
            VideoPlayer(uri = mediaFile.path)
        }

        MediaType.VIDEO -> if (mediaFile != null) {
            VideoPlayer(uri = mediaFile.path)
        }

    }
}


@Composable
fun ImageViewer(imageContent: ImageBitmap) {
    i("ImageViewer imageContent=$imageContent")

    Image(
        bitmap = imageContent,
        contentDescription = "post image",
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    )
}

fun byteArrayToImageBitmap(imageBytes: ByteArray): ImageBitmap {
    val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    return decodedImage.asImageBitmap()
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
private fun PostCardBuilderPreview() {
    MaterialTheme {
        Column {
            PostCardBuilder(
                FakeDataGenerator().generateSingleSamplePost("name", MediaType.IMAGE),
                onPostClick = { },
                onPostLongClick = {}
            )
        }
    }
}