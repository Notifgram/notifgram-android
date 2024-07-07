package com.notifgram.core.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.File

@Parcelize
data class Post(
    var id: Int = 0,
    var channelId: Int,
    var text: String = "",
    val mediaFile: File?,
    var mediaType: MediaType,

) : Parcelable {
    companion object {
        fun newInstance() = Post(
            channelId = 0,
            text = "",
            mediaFile = null,
            mediaType = MediaType.NO_MEDIA,
        )
    }

}

enum class MediaType{
    IMAGE,
    VIDEO,
    AUDIO,
    NO_MEDIA
}

fun Post.doesMatchSearchQuery(query: String): Boolean {
    val matchingCombinations = listOf(
        text,
    )

    return matchingCombinations.any {
        it.contains(query, ignoreCase = true)
    }
}