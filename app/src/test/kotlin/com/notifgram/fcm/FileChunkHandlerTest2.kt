package com.notifgram.fcm

import com.notifgram.core.common.FileEncryptionManager
import com.notifgram.core.common.MyLog
import com.notifgram.core.common.TestLogger
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import java.io.File

class FileChunkHandlerTest2 {

    @MockK
    private var fileEncryptionManager: FileEncryptionManager = mockk()

    private var fileChunkHandler: FileChunkHandler = FileChunkHandler()

    private val totalChunks = 3

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true) // turn relaxUnitFun on for all mocks
        MyLog.logger = TestLogger()
        fileChunkHandler.fileEncryptionManager = fileEncryptionManager
    }

    @Test
    fun getAllChunksTest() {
        println("start test")

        val areChunksEncrypted = true

//        every { mockFileSrc.copyTo(any(), any(), any()) } answers {
//            arg(1) as File  // arg(0) is `this` (source), arg(1) is dest
//        }

//        println("done initializing mocks")

//        assertTrue(mockFileSrc.exists())
//        mockFileSrc.copyTo(mockFileDst, true, 0)

//        val mockedFile = mockk<File>()
//        every { File(anyString(), anyString()) } returns mockedFile
//        justRun { any<File>().readBytes() }

        mockkStatic(File::readBytes)
        every { any<File>().readBytes() } returns byteArrayOf()

        every { fileEncryptionManager.decryptByteArray(any()) } returns byteArrayOf() // or whatever you want it to return
        fileChunkHandler.getAllChunks("", totalChunks, areChunksEncrypted)
//        justRun { fileEncryptionManager.decryptByteArray(any()) }

//        verify( ) {fileChunkHandler.getAllChunks(anyString(), anyInt(),true)}
        verify(exactly = totalChunks) { fileEncryptionManager.decryptByteArray(any()) }

        println("done with test")
    }
}
