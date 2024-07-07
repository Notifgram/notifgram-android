package com.notifgram.core.data_repository.sync

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class LastRemoteChange(
    val id: Int,
    val timestamp: String,   // is DateTime type
    val isDelete: Boolean,
)
