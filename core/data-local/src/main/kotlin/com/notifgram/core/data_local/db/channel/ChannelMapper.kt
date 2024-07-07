package com.notifgram.core.data_local.db.channel

import com.notifgram.core.domain.entity.Channel

fun Channel.toChannelEntity(): ChannelEntity {
    return ChannelEntity(
        id = id,
        description = description,
        name = name,
        isFollowed = isFollowed
    )
}

fun ChannelEntity.toChannel(): Channel {
    return Channel(
        id = id,
        description = description,
        name = name,
        isFollowed = isFollowed

    )
}