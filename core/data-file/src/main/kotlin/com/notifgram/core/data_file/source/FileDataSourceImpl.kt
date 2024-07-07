package com.notifgram.core.data_file.source

import android.content.Context
import com.notifgram.core.common.FileEncryptionManager
import com.notifgram.core.common.MyLog.e
import com.notifgram.core.common.MyLog.i
import com.notifgram.core.data_repository.data_source.file.FileDataSource
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
import javax.inject.Named

class FileDataSourceImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val fileEncryptionManager: FileEncryptionManager,
    @Named("CachedFilesDir") val cachedFilesDir: File
) : FileDataSource {

    override fun writeToFile(outputFile: File, fileContent: ByteArray) {
        i("$TAG writeToFile fileContent=$fileContent outputFile=$outputFile")
        FileOutputStream(outputFile).use { output ->
            output.write(fileContent)
        }
    }

    override fun readFile(file: File): ByteArray {
        i("$TAG readFile() file.name=${file.name}")
        val result = file.readBytes()
        i("$TAG readFile() result=$result")
        return result
    }

    // TODO: rename to readFileDecrypted
    override fun openFileDecrypted(fileName: String): ByteArray? {
        i("$TAG openFileDecrypted fileName=$fileName")
        val dirPath = context.filesDir.path.plus("/")
            .plus(fileName.replace('.', '-')) // files/file-format/
        i("$TAG openFileDecrypted dirPath=$dirPath")
        val file = File(dirPath, fileName)
        val result = openFileDecrypted(file)
        i("$TAG openFileDecrypted result=$result")
        return result
    }

    // TODO: rename to readFileDecrypted
    override fun openFileDecrypted(file: File): ByteArray? {
        i("$TAG openFileDecrypted file=$file")

        if (!file.exists()) {
            e("file does not exist.")
            return null
        }
        val decryptedContent = fileEncryptionManager.decryptByteArray(
            file.readBytes()
        )

        i("$TAG openFileDecrypted decryptedContent.size=${decryptedContent.size}")
        return decryptedContent
    }

    override fun createTempFile(
        fileName: String,
        byteArray: ByteArray,
//                       context:Context
    ): File {
        i("$TAG createTempFile fileName=$fileName byteArray=$byteArray")
        val byteArrayInputStream = ByteArrayInputStream(byteArray)

        val fileNamePrefix = fileName.split(".")[0]
        val format = fileName.split(".")[1]

        i("$TAG createTempFile fileName=$fileName")


        // Creates a temp file which will be deleted after closing app.
        // Name of the created temp file will be [prefix] [random] [suffix]
        val tempFilePath = kotlin.io.path.createTempFile(fileNamePrefix, format)
        val tempFile = File(tempFilePath.toUri())
        FileOutputStream(tempFile).use { outputStream ->
            byteArrayInputStream.use { inputStream ->
                inputStream.copyTo(outputStream)
            }
        }
        val secureFile = File(context.filesDir, fileName)

        // Rename the file from random to the real name of the file.
        tempFile.renameTo(secureFile)

        i("$TAG createTempFile tempFile=${tempFile.absoluteFile}")
        return secureFile
    }

    override fun deleteCachedFilesDirectory() {
        cachedFilesDir.deleteRecursively()

    }

    companion object {
        private const val TAG = "FileDataSourceImpl"
    }

}