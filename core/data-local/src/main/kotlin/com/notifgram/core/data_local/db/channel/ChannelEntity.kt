package com.notifgram.core.data_local.db.channel

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ChannelEntity(
    @PrimaryKey val id: Int,
    val name: String = "",
    val description: String = "",
    val isFollowed: Boolean = false,

    )