package com.notifgram.core.data_local.db.post

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PostEntity(
    @PrimaryKey val id: Int,
    val channelId: Int,
    val text: String = "",
    val fileName: String? = "",
    val mediaType: Int,

    )