package com.notifgram.fcm

import java.io.File
import java.io.FileInputStream
import java.security.MessageDigest

fun getFileHash(file: File): String {
    val inputStream = FileInputStream(file)
    val buffer = ByteArray(1200000)
    val digest = MessageDigest.getInstance("SHA-256")

    while (true) {
        val bytesRead = inputStream.read(buffer)
        if (bytesRead <= 0) break
        digest.update(buffer, 0, bytesRead)
    }

    val hashBytes = digest.digest()
    return hashBytes.joinToString("") { "%02x".format(it) }
}

fun getFileHash(filePath: String): String {
    val file = File(filePath)
    val inputStream = FileInputStream(file)
    val buffer = ByteArray(1200000)
    val digest = MessageDigest.getInstance("SHA-256")

    while (true) {
        val bytesRead = inputStream.read(buffer)
        if (bytesRead <= 0) break
        digest.update(buffer, 0, bytesRead)
    }

    val hashBytes = digest.digest()
    return hashBytes.joinToString("") { "%02x".format(it) }
}