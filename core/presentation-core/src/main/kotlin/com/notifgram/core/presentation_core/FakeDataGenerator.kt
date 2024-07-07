package com.notifgram.core.presentation_core

import com.notifgram.core.common.MyLog.i
import com.notifgram.core.domain.entity.Channel
import com.notifgram.core.domain.entity.MediaType
import com.notifgram.core.domain.entity.Post
import java.io.File

class FakeDataGenerator {

    fun generateSamplePosts(): List<Post> {
        return listOf(
            Post(
                id = (1 until 10000).random(),
                channelId = 0,
                text = "text- 1 chunk image",
                mediaType = MediaType.IMAGE,
                mediaFile = File("1.jpg")
            ),
            Post(
                id = (1 until 10000).random(),
                channelId = 0,
                text = "text- 2 chunks image",
                mediaType = MediaType.IMAGE,
                mediaFile = File("2.jpg")
            ),
            Post(
                id = (1 until 10000).random(),
                channelId = 0,
                text = "text- is audio",
                mediaType = MediaType.AUDIO,
                mediaFile = File("a.mp3")
            ),
            Post(
                id = (1 until 10000).random(),
                channelId = 0,
                text = "text- is video",
                mediaType = MediaType.VIDEO,
                mediaFile = File("a.mp4")
            ),
        )
    }

    fun generateSamplePosts(count: Int): List<Post> {
        val posts: MutableList<Post> = ArrayList<Post>()
        for (i in 1..count) {
            posts.add(
                generateSingleSamplePost(
                    "text $i",
                    MediaType.values()[i % MediaType.values().size]
                )
            )
        }
        i("finished")
        return posts
    }

    fun generateSingleSamplePost(
        text: String,
        mediaType: MediaType
    ): Post {
        return Post(
            id = (1 until 10000).random(),
            channelId = 0,
            text = text,
            mediaType = mediaType,
//            mediaContent = null,
            mediaFile = when (mediaType) {
                MediaType.IMAGE -> File("imageFile.jpg")
                MediaType.AUDIO -> File("audioFile.mp3")
                MediaType.VIDEO -> File("videoFile.mp4")
                else -> null
            }
        )
    }

    fun generateSampleChannels(count: Int): List<Channel> {
        val channels: MutableList<Channel> = ArrayList<Channel>()
        for (i in 1..count) {
            channels.add(
                generateSingleSampleChannel(
                    "name $i",
                    "description$i"
                )
            )
        }
        i("finished")
        return channels
    }

    fun generateSingleSampleChannel(
        name: String,
        description: String
    ): Channel {
        return Channel(
            id = (1 until 10000).random(),
            name = name,
            description = description,
            isFollowed = false
        )
    }

}