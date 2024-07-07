package com.notifgram.core.data_remote.networking.channel

import com.notifgram.core.domain.entity.Channel

fun ChannelDto.toChannel(): Channel {
    if (id == null)
        throw Exception("Channel Id received from remote is null")

    return Channel(
        id = id,
        name = name,
        description = description,
        isFollowed = false
    )
}

fun Channel.toChannelDto(): ChannelDto {
    return ChannelDto(
        id = id,
        name = name,
        description = description,
    )
}