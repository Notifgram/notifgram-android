package com.notifgram.core.domain

import com.notifgram.core.domain.entity.MediaType
import com.notifgram.core.domain.entity.Post
import com.notifgram.core.domain.usecase.post.LoadPostsUseCase
import com.notifgram.core.test.repository.TestPostRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.kotlin.mock
import java.io.File
import kotlin.test.assertEquals

class LoadPostsUseCaseTest {

    private val testPostRepository = TestPostRepository()

    private val useCase = LoadPostsUseCase(
        mock(),
        postRepository = testPostRepository,
    )

    @Test
    fun returns_data_correctly() = runTest {
        testPostRepository.insertPostsLocally(testPosts)

        assertEquals(
            useCase.process(LoadPostsUseCase.Request).first().posts,
            testPosts
        )
    }
}

private val testPosts = listOf(
    Post(
        id = 1,
        channelId = 0,
        text = "text- is image",
        mediaType = MediaType.IMAGE,
        mediaFile = File("image.jpg")
    ),
    Post(
        id = 2,
        channelId = 0,
        text = "text- is audio",
        mediaType = MediaType.AUDIO,
        mediaFile = File("voice.mp3")
    ),
    Post(
        id = 3,
        channelId = 0,
        text = "text- is video",
        mediaType = MediaType.VIDEO,
        mediaFile = File("video.mp4")
    ),
)