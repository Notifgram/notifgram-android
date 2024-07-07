package com.notifgram.fcm

import com.notifgram.core.common.MyLog.i
import java.io.File
import java.io.FileOutputStream

private const val TAG = "FileOperationsHelper"
fun deleteFilesMatchingRegex(directoryPath: String, regex: String) {
    i("$TAG deleteFilesMatchingRegex()  directoryPath=$directoryPath regex=$regex")
    val directory = File(directoryPath)

    if (!directory.exists() || !directory.isDirectory) {
        throw IllegalArgumentException("Invalid directory path")
    }

    val regexPattern = regex.toRegex()

    directory.listFiles { file ->
        file.isFile && regexPattern.matches(file.name)
    }?.forEach { it.delete() }
}

fun writeToFile(outputFile: File, fileContent: ByteArray) {
    i("$TAG writeToFile fileContent=$fileContent outputFile=$outputFile")
    FileOutputStream(outputFile).use { output ->
        output.write(fileContent)
    }
}

/**
 * Counts number of files matching regex inside directoryPath
 */
fun countMatchingFiles(directoryPath: String, regex: String): Int {
    i("$TAG countMatchingFiles()  directoryPath=$directoryPath regex=$regex")
    val directory = File(directoryPath)

    if (!directory.exists() || !directory.isDirectory)
        throw IllegalArgumentException("Invalid directory path")


    val regexPattern = regex.toRegex()

    val matchingFiles = directory.listFiles { file ->
        file.isFile && regexPattern.matches(file.name)
    }

    val count = matchingFiles?.size ?: 0
    i("$TAG.countMatchingFiles()  count=$count")
    return count
}

fun createDirectoryToStoreFile(dirPath: String) {
    val file = File(dirPath) // files/filename-format/

    if (!file.exists())
        file.mkdirs()
}


