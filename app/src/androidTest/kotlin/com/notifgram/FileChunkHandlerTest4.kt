package com.notifgram

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.notifgram.core.common.FileEncryptionManager
import com.notifgram.core.common.MyLog
import com.notifgram.core.common.TestLogger
import com.notifgram.fcm.FileChunkHandler
import com.notifgram.fcm.getFileHash
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

@RunWith(AndroidJUnit4::class)
class FileChunkHandlerTest4 {

    private var fileChunkHandler: FileChunkHandler = FileChunkHandler()

    @MockK
    private var fileEncryptionManager: FileEncryptionManager = mockk()

    private lateinit var appContext: Context
    private lateinit var tempDir: File
    private lateinit var tempFile: File

    @Before
    fun setUp() {
        appContext = InstrumentationRegistry.getInstrumentation().targetContext
        tempDir = appContext.cacheDir

        MyLog.logger = TestLogger()
        every { fileEncryptionManager.decryptByteArray(any()) } returns byteArrayOf()
        tempDir = createTempDir("testDir")
        tempFile = File(tempDir, "testFile.txt")
        tempFile.writeText("This is a test file content.")
        fileChunkHandler.fileEncryptionManager = fileEncryptionManager

    }

    @After
    fun tearDown() {
        tempFile.delete()
        tempDir.deleteRecursively()
    }

    @Test
    fun testLogFinalFileWithoutEncryption() {
        // Arrange
        val shouldEncrypt = false
        val expectedOutput = listOf(
            "${tempFile.name} hash of created local final file=" + getFileHash(tempFile),
            "${tempFile.name} size of created local final file=${tempFile.length()} Bytes"
        )

        // Act
        fileChunkHandler.logFinalFile(tempFile, shouldEncrypt)

        // Assert
        expectedOutput.forEach { expected ->
            assertEquals(true, capturedLogs.contains(expected))
        }
    }

    @Test
    fun testLogFinalFileWithEncryption() {
        // Arrange
        val shouldEncrypt = true
        val expectedOutput = listOf(
            "${tempFile.name} hash of created local final file=" + getFileHash(tempFile),
            "${tempFile.name} size of created local final file=${tempFile.length()} Bytes"
        )

        // Act
        fileChunkHandler.logFinalFile(tempFile, shouldEncrypt)

        // Assert
        expectedOutput.forEach { expected ->
            assertEquals(true, capturedLogs.contains(expected))
        }
    }

    // Helper function to simulate log capture
    companion object {
        val capturedLogs = mutableListOf<String>()

        fun i(log: String) {
            capturedLogs.add(log)
        }
    }
}
