package com.notifgram.core.data_remote.networking.channel

import com.notifgram.core.domain.entity.Channel
import org.junit.Test
import kotlin.test.assertEquals

class ChannelMapperTest {
    @Test
    fun channelEntity_to_channel_test() {
        val channelDto = ChannelDto(
            id = 1,
            description = "",
            name = "",
        )
        val channel = channelDto.toChannel()

        assertEquals(
            Channel(
                id = 1,
                isFollowed = false,
                description = "",
                name = "",
            ),
            channel,
        )
    }

    @Test
    fun channel_to_channelEntity_test() {
        val channel = Channel(
            id = 1,
            isFollowed = false,
            description = "",
            name = "",
        )
        val channelEntity = channel.toChannelDto()
        assertEquals(
            ChannelDto(
                id = 1,
                description = "",
                name = "",
            ),
            channelEntity,
        )
    }

}
