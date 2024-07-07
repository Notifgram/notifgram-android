package com.notifgram.fcm

import com.notifgram.core.common.FileEncryptionManager
import com.notifgram.core.common.MyLog
import com.notifgram.core.common.TestLogger
import com.notifgram.core.domain.entity.settings.AppSettings
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.doNothing
import org.mockito.kotlin.spy
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import java.io.File

@RunWith(MockitoJUnitRunner::class)
class FileChunkHandlerTest {

    @Mock
    private lateinit var cachedFilesDir: File

    @Mock
    private lateinit var appSettings: AppSettings

    @Mock
    private lateinit var fileEncryptionManager: FileEncryptionManager

    private var fileChunkHandler: FileChunkHandler = spy(FileChunkHandler())

    @Mock
    private var file: File = spy(File(""))

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)

        // Set the logger to the mock
        MyLog.logger = TestLogger()

        // Stub the logFinalFile method
        doNothing().`when`(fileChunkHandler).logFinalFile(any<File>(), any<Boolean>())

        fileChunkHandler.cachedFilesDir = cachedFilesDir
        fileChunkHandler.appSettings = appSettings
        fileChunkHandler.fileEncryptionManager = fileEncryptionManager

        file = File("")
    }

    @Test
    fun testHandleFileChunk() {
        // Prepare test data
        val data = mapOf(
            FcmKeys.FILE_NAME.toString() to "testFile",
            FcmKeys.FILE_CHUNK_INDEX.toString() to "0",
            FcmKeys.FILE_TOTAL_CHUNKS.toString() to "1",
            FcmKeys.FILE_CHUNK_DATA.toString() to "testData"
        )

        // Set up mocks
        `when`(appSettings.shouldEncrypt).thenReturn(true)
        `when`(appSettings.shouldEncryptChunks).thenReturn(true)
        `when`(fileEncryptionManager.encryptByteArray(anyOrNull())).thenReturn(byteArrayOf())

        // Call method under test
        fileChunkHandler.handleFileChunk(data)

        // Verify interactions
        verify(fileEncryptionManager).encryptByteArray(anyOrNull())
    }

    @Test
    fun testHandleFileChunk2() {
        val file_total_chunks = "2"
        val file_chunk_data = "testData"

        // Prepare test data
        val data = mapOf(
            FcmKeys.FILE_NAME.toString() to "testFile",
            FcmKeys.FILE_CHUNK_INDEX.toString() to "0",
            FcmKeys.FILE_TOTAL_CHUNKS.toString() to file_total_chunks,
            FcmKeys.FILE_CHUNK_DATA.toString() to file_chunk_data
        )

        // Set up mocks
        `when`(appSettings.shouldEncrypt).thenReturn(true)
        `when`(appSettings.shouldEncryptChunks).thenReturn(true)
        `when`(fileEncryptionManager.encryptByteArray(anyOrNull())).thenReturn(byteArrayOf())

        // Call method under test
        fileChunkHandler.handleFileChunk(data)

        // Verify interactions
        verify(fileEncryptionManager).encryptByteArray(anyOrNull())
        val decodedChunkDataByteArray = decodeChunk(file_chunk_data)
        verify(fileEncryptionManager, times(1)).encryptByteArray(decodedChunkDataByteArray)
    }


}
