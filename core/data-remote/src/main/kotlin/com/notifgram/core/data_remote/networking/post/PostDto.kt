package com.notifgram.core.data_remote.networking.post

import androidx.annotation.Keep
import com.squareup.moshi.Json
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Keep
data class PostDto(
    @SerialName("Id") @field:Json(name = "id") val id: Int?,
    @SerialName("ChannelId") @field:Json(name = "channelId") val channelId: Int,
    @SerialName("Text") @field:Json(name = "text") val text: String,
    @SerialName("FileName") @field:Json(name = "fileName") val fileName: String,
    @SerialName("MediaType") @field:Json(name = "mediaType") val mediaType: Int,
)
