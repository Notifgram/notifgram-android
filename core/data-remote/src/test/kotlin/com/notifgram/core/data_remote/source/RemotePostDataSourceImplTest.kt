package com.notifgram.core.data_remote.source

import com.notifgram.core.data_remote.networking.post.PostApi
import com.notifgram.core.data_remote.networking.post.PostDto
import com.notifgram.core.data_repository.FileUriBuilder
import com.notifgram.core.domain.entity.MediaType
import com.notifgram.core.domain.entity.Post
import com.notifgram.core.domain.entity.UseCaseException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class RemotePostDataSourceImplTest {

    private val postApi = mock<PostApi>()
    private val fileUriBuilder = mock<FileUriBuilder>()
    private val postDataSource = RemotePostDataSourceImpl(postApi, fileUriBuilder)

    @ExperimentalCoroutinesApi
    @Test
    fun testGetPosts() = runTest {
        val remotePosts = listOf(PostDto(1, 1, "text", "1.jpg", -1))
        val expectedPosts =
            listOf(Post(1, 1, "text", fileUriBuilder.buildFile("1.jpg"), MediaType.IMAGE))
        whenever(postApi.getPosts()).thenReturn(remotePosts)
        val result = postDataSource.getPosts().first()
        Assert.assertEquals(expectedPosts, result)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun testGetPost() = runTest {
        val id = 1
        val remotePost = PostDto(1, 1, "text", "1.jpg", -1)
        val expectedPost = Post(1, 1, "text", fileUriBuilder.buildFile("1.jpg"), MediaType.IMAGE)
        whenever(postApi.getPost(id)).thenReturn(remotePost)
        val result = postDataSource.getPost(id).first()
        Assert.assertEquals(expectedPost, result)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun testGetPostsThrowsError() = runTest {
        whenever(postApi.getPosts()).thenThrow(RuntimeException())
        postDataSource.getPosts().catch {
            Assert.assertTrue(it is UseCaseException.PostException)
        }.collect()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun testGetPostThrowsError() = runTest {
        val id = 1
        whenever(postApi.getPost(id)).thenThrow(RuntimeException())
        postDataSource.getPost(id).catch {
            Assert.assertTrue(it is UseCaseException.PostException)
        }.collect()
    }
}