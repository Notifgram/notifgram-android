package com.notifgram.core.data_repository.data_source.file

import java.io.File

interface FileDataSource {
    fun writeToFile(outputFile: File, fileContent: ByteArray)

//    fun readFile(fileName: String): ByteArray?
fun readFile(file: File): ByteArray
    fun openFileDecrypted(fileName: String): ByteArray?
    fun openFileDecrypted(file: File): ByteArray?

    fun createTempFile(fileName: String, byteArray: ByteArray): File
    fun deleteCachedFilesDirectory()

}