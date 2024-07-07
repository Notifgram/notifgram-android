package com.notifgram.fcm

import com.notifgram.core.common.MyLog
import com.notifgram.core.common.TestLogger
import org.junit.Assert.assertArrayEquals
import org.junit.Before
import org.junit.Test

class FileChunkHandlerTest3 {

    private var fileChunkHandler: FileChunkHandler = FileChunkHandler()

    @Before
    fun setUp() {
        MyLog.logger = TestLogger()
    }

    @Test
    fun mergeFileChunks_emptyInput_returnsEmptyArray() {
        val inputChunks = mutableListOf<ByteArray>()
        val mergedContent = fileChunkHandler.mergeFileChunks(inputChunks)
        assertArrayEquals(ByteArray(0), mergedContent)
    }

    @Test
    fun mergeFileChunks_singleChunk_returnsSameChunk() {
        val inputChunks = mutableListOf(byteArrayOf(1, 2, 3, 4))
        val mergedContent = fileChunkHandler.mergeFileChunks(inputChunks)
        assertArrayEquals(byteArrayOf(1, 2, 3, 4), mergedContent)
    }

    @Test
    fun mergeFileChunks_multipleChunks_returnsMergedContent() {
        val inputChunks = mutableListOf(
            byteArrayOf(1, 2, 3),
            byteArrayOf(4, 5),
            byteArrayOf(6, 7, 8)
        )
        val expectedMergedContent = byteArrayOf(1, 2, 3, 4, 5, 6, 7, 8)
        val mergedContent = fileChunkHandler.mergeFileChunks(inputChunks)
        assertArrayEquals(expectedMergedContent, mergedContent)
    }
}
