package com.notifgram.core.data_local.db.post

import com.notifgram.core.common.MyLog
import com.notifgram.core.common.TestLogger
import com.notifgram.core.data_repository.FileUriBuilder
import com.notifgram.core.data_repository.convertIntToMediaType
import com.notifgram.core.domain.entity.Post
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import org.junit.Before
import org.junit.Test
import java.io.File
import kotlin.test.assertEquals

class PostMapperTest {

    @MockK
    val fileUriBuilder: FileUriBuilder = mockk()

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true) // turn relaxUnitFun on for all mocks
        MyLog.logger = TestLogger()
    }

    @Test
    fun postEntity_to_post_test() {
        val file = File("fileName")
        every { fileUriBuilder.buildFile(any()) } returns file
        val postDto = PostEntity(
            id = 1,
            channelId = 1,
            text = "text",
            fileName = "fileName",
            mediaType = -1
        )
        val post = postDto.toPost(fileUriBuilder)

        assertEquals(
            Post(
                id = 1,
                channelId = 1,
                text = "text",
                mediaFile = file,
                mediaType = convertIntToMediaType(-1)
            ),
            post,
        )
    }

    @Test
    fun post_to_postEntity_test() {
        val post = Post(
            id = 1,
            channelId = 1,
            text = "text",
            mediaFile = File("file"),
            mediaType = convertIntToMediaType(-1)
        )
        val postEntity = post.toPostEntity()
        assertEquals(
            PostEntity(
                id = 1,
                channelId = 1,
                text = "text",
                fileName = "file",
                mediaType = -1
            ),
            postEntity,
        )
    }

}
