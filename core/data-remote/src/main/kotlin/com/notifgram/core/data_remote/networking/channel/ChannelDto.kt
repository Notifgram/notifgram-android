package com.notifgram.core.data_remote.networking.channel

import androidx.annotation.Keep
import com.squareup.moshi.Json
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Keep
data class ChannelDto(
    @SerialName("Id") @field:Json(name = "id") val id: Int?,
    @SerialName("Name") @field:Json(name = "name") val name: String,
    @SerialName("Description") @field:Json(name = "description") val description: String,
)
