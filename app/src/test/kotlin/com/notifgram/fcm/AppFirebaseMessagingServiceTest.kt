package com.notifgram.fcm

import com.notifgram.core.common.MyLog
import com.notifgram.core.common.TestLogger
import com.notifgram.core.data_remote.networking.channel.ChannelDto
import com.notifgram.core.data_remote.networking.post.PostDto
import com.notifgram.core.data_repository.FileUriBuilder
import com.notifgram.core.data_repository.repository.ChannelRepositoryImpl
import com.notifgram.core.data_repository.repository.PostRepositoryImpl
import com.notifgram.core.data_repository.sync.SyncManager
import com.google.firebase.messaging.RemoteMessage
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class AppFirebaseMessagingServiceTest {

    // Mock dependencies
    private val mockSyncManager: SyncManager = mockk(relaxed = true)
    private val mockPostRepository: PostRepositoryImpl = mockk(relaxed = true)
    private val mockChannelRepository: ChannelRepositoryImpl = mockk(relaxed = true)
    private val mockFileUriBuilder: FileUriBuilder = mockk(relaxed = true)
    private val mockFileChunkHandler: FileChunkHandler = mockk(relaxed = true)
    private val mockJson: Json = mockk(relaxed = true)
    private val realJson = Json { ignoreUnknownKeys = true }
    // Class under test
    private var firebaseMessagingService: AppFirebaseMessagingService =
        AppFirebaseMessagingService()

    @Before
    fun setUp() {
        MyLog.logger = TestLogger()

        firebaseMessagingService.postRepositoryImpl = mockPostRepository
        firebaseMessagingService.channelRepositoryImpl = mockChannelRepository
        firebaseMessagingService.fileUriBuilder = mockFileUriBuilder
        firebaseMessagingService.fileChunkHandler = mockFileChunkHandler

        firebaseMessagingService.syncManager = mockSyncManager
        firebaseMessagingService.json = mockJson
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `onMessageReceived should sync when message is from SYNC_REQUEST_TOPIC`() = runTest {
        // Given
        val remoteMessage: RemoteMessage = mockk(relaxed = true) {
            every { from } returns "/topics/${FcmTopics.RESTFUL_SYNC_REQUEST_TOPIC}"
        }

        // When
        firebaseMessagingService.onMessageReceived(remoteMessage)

        // Then
        coVerify { mockSyncManager.sync() }

        // should not call these:
        coVerify(exactly = 0) { mockChannelRepository.insertChannelLocally(any()) }
        coVerify(exactly = 0) { mockPostRepository.insertPostLocally(any()) }
        coVerify(exactly = 0) { mockFileChunkHandler.handleFileChunk(any()) }
    }

    @Test
    fun `onMessageReceived should not sync when message is not from SYNC_REQUEST_TOPIC`() =
        runTest {
            // Given
            val remoteMessage: RemoteMessage = mockk(relaxed = true) {
                every { from } returns "/topics/other_topic"
            }

            // When
            firebaseMessagingService.onMessageReceived(remoteMessage)

            // Then
            coVerify(exactly = 0) { mockSyncManager.sync() }
        }

    @Test
    fun `onMessageReceived when notification is null`() = runTest {
        // Given
        val remoteMessage: RemoteMessage = mockk(relaxed = true) {
            every { from } returns "/topics/other_topic"
            every { notification } returns null
        }

        // When
        firebaseMessagingService.onMessageReceived(remoteMessage)

        // Then
        coVerify(exactly = 0) { mockSyncManager.sync() }
    }

    @Test
    fun `onMessageReceived should call handleFileChunk when data is a file`() = runTest {
        // Given
        val remoteMessage: RemoteMessage = mockk(relaxed = true) {
            every { from } returns "/topics/other_topic"
            every { notification } returns null
            every { data } returns mapOf(FcmKeys.FILE_NAME.toString() to "fileName")
        }

        // When
        firebaseMessagingService.onMessageReceived(remoteMessage)

        // Then
        coVerify { mockFileChunkHandler.handleFileChunk(any()) }
        coVerify(exactly = 0) { mockSyncManager.sync() }
    }

    @Test
    fun `onMessageReceived should call upsertPostLocally when data is a post object`() = runTest {
        // Given
        val remoteMessage: RemoteMessage = mockk(relaxed = true) {
            every { from } returns "/topics/other_topic"
            every { notification } returns null
            every { data } returns mapOf(FcmKeys.JSON_POST.toString() to "")
        }

        every { mockJson.decodeFromString<PostDto>(any()) } returns PostDto(1, 1, "", "", 1)

        // When
        firebaseMessagingService.onMessageReceived(remoteMessage)

        // Then
        coVerify { mockPostRepository.upsertPostLocally(any()) }

        coVerify(exactly = 0) { mockSyncManager.sync() }
        coVerify(exactly = 0) { mockChannelRepository.insertChannelLocally(any()) }
        coVerify(exactly = 0) { mockFileChunkHandler.handleFileChunk(any()) }

    }

    @Test
    fun `onMessageReceived should call upsertChannelLocally when data is a channel object`() =
        runTest {
            // Given
            val remoteMessage: RemoteMessage = mockk(relaxed = true) {
                every { from } returns "/topics/other_topic"
                every { notification } returns null
                every { data } returns mapOf(FcmKeys.JSON_CHANNEL.toString() to "")
            }

            every { mockJson.decodeFromString<ChannelDto>(any()) } returns ChannelDto(1, "", "")

            // When
            firebaseMessagingService.onMessageReceived(remoteMessage)

            // Then
            coVerify { mockChannelRepository.upsertChannelLocally(any()) }

            coVerify(exactly = 0) { mockSyncManager.sync() }
            coVerify(exactly = 0) { mockFileChunkHandler.handleFileChunk(any()) }

        }

    @Test
    fun `onMessageReceived should call deleteChannelLocally when data contains DELETED_CHANNEL`() =
        runTest {
            // Given
            val remoteMessage: RemoteMessage = mockk(relaxed = true) {
                every { from } returns "/topics/other_topic"
                every { notification } returns null
                every { data } returns mapOf(FcmKeys.DELETED_CHANNEL.toString() to "1")
            }

            // When
            firebaseMessagingService.onMessageReceived(remoteMessage)

            // Then
            coVerify { mockChannelRepository.deleteChannelLocally(any()) }

            coVerify(exactly = 0) { mockSyncManager.sync() }
            coVerify(exactly = 0) { mockFileChunkHandler.handleFileChunk(any()) }

        }

    @Test
    fun `onMessageReceived should call deletePostLocally when data contains DELETED_POST`() =
        runTest {
            // Given
            val remoteMessage: RemoteMessage = mockk(relaxed = true) {
                every { from } returns "/topics/other_topic"
                every { notification } returns null
                every { data } returns mapOf(FcmKeys.DELETED_POST.toString() to "1")
            }

            // When
            firebaseMessagingService.onMessageReceived(remoteMessage)

            // Then
            coVerify { mockPostRepository.deletePostLocally(any()) }

            coVerify(exactly = 0) { mockSyncManager.sync() }
            coVerify(exactly = 0) { mockFileChunkHandler.handleFileChunk(any()) }

        }

    @Test
    fun `onMessageReceived should call upsertChannelLocally when data contains JSON_ALL_CHANNEL`() =
        runTest {
            // Given
            val remoteMessage: RemoteMessage = mockk(relaxed = true) {
                every { from } returns "/topics/other_topic"
                every { notification } returns null
                every { data } returns mapOf(FcmKeys.JSON_ALL_CHANNEL.toString() to "")
            }

            val list = listOf(
                ChannelDto(1, "", ""),
                ChannelDto(2, "", "")
            )
            every { mockJson.decodeFromString<List<ChannelDto>>(any()) } returns list

            // When
            firebaseMessagingService.onMessageReceived(remoteMessage)

            // Then
            coVerify(exactly = list.size) { mockChannelRepository.upsertChannelLocally(any()) }

            coVerify(exactly = 0) { mockSyncManager.sync() }
            coVerify(exactly = 0) { mockFileChunkHandler.handleFileChunk(any()) }

        }

    @Test
    fun `convertJsonObjectToPostDto decodes correctly`() = runTest {
        // Given
        firebaseMessagingService.json = realJson

        val expectedPostDto = PostDto(1, 1, "text", "fileName", 1)
        val data =
            "{\"Id\":1,\"ChannelId\":1,\"Text\":\"text\",\"FileName\":\"fileName\",\"MediaType\":1}"
        // When
        val decodedPostDto = firebaseMessagingService.convertJsonObjectToPostDto(data)

        // Then
        assertEquals(decodedPostDto, expectedPostDto)
    }

    @Test
    fun `convertJsonObjectToChannelDto decodes correctly`() = runTest {
        // Given
        firebaseMessagingService.json = realJson

        val expectedChannelDto = ChannelDto(1, "Name", "Description")
        val data =
            "{\"Id\":1,\"Name\":\"Name\",\"Description\":\"Description\"}"
        // When
        val decodedChannelDto = firebaseMessagingService.convertJsonObjectToChannelDto(data)

        // Then
        assertEquals(decodedChannelDto, expectedChannelDto)
    }
}