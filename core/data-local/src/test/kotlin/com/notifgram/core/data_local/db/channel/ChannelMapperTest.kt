package com.notifgram.core.data_local.db.channel

import com.notifgram.core.domain.entity.Channel
import org.junit.Test
import kotlin.test.assertEquals

class ChannelMapperTest {
    @Test
    fun channelEntity_to_channel_test() {
        val entity = ChannelEntity(
            id = 1,
            isFollowed = false,
            description = "",
            name = "",
        )
        val channel = entity.toChannel()

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
        val channelEntity = channel.toChannelEntity()
        assertEquals(
            ChannelEntity(
                id = 1,
                isFollowed = false,
                description = "",
                name = "",
            ),
            channelEntity,
        )
    }

}
