package com.notifgram.fcm

import com.notifgram.core.common.FileEncryptionManager
import com.notifgram.core.common.MyLog.e
import com.notifgram.core.common.MyLog.i
import com.notifgram.core.domain.entity.settings.AppSettings
import java.io.File
import java.util.Base64
import javax.inject.Inject
import javax.inject.Named

fun decodeChunk(fileChunkData: String): ByteArray {
    val decoder: Base64.Decoder = Base64.getDecoder()
    return decoder.decode(fileChunkData)
}

class FileChunkHandler @Inject constructor() {

    @Inject
    @Named("CachedFilesDir")
    lateinit var cachedFilesDir: File

    @Inject
    lateinit var appSettings: AppSettings

    @Inject
    lateinit var fileEncryptionManager: FileEncryptionManager

    private val tempFilesRegex = ".*\\.part"


    fun handleFileChunk(data: Map<String, String>) {
        val shouldEncrypt = appSettings.shouldEncrypt
        val shouldEncryptChunks = appSettings.shouldEncryptChunks

        i("$TAG onMessageReceived shouldEncrypt=${appSettings.shouldEncrypt} shouldEncryptChunks=${appSettings.shouldEncryptChunks}")


        if (!isFileMessageValid(data)) {
            e("$TAG handleFileChunk() message content is not valid")
            return
        }
        val fileName = data[FcmKeys.FILE_NAME.toString()]!!
        val chunkIndex = data[FcmKeys.FILE_CHUNK_INDEX.toString()]!!.toInt()
        val totalChunks = data[FcmKeys.FILE_TOTAL_CHUNKS.toString()]!!.toInt()
        val fileChunkData = data[FcmKeys.FILE_CHUNK_DATA.toString()]!!

        val decodedChunkDataByteArray = decodeChunk(fileChunkData)
        val dirPath =
            cachedFilesDir.path.plus("/").plus(fileName.replace('.', '-')) // files/file-format/

        createDirectoryToStoreFile(dirPath)


        var finalFileContent: ByteArray = decodedChunkDataByteArray
        if (totalChunks > 1) {
            i("totalChunks=$totalChunks")
            var chunk = decodedChunkDataByteArray
            if (shouldEncryptChunks)
                chunk = fileEncryptionManager.encryptByteArray(finalFileContent)

            val file = File(
                dirPath, chunkIndex.toString().plus(".part")
            ) // files/filename-format/filename.part
            if (!file.exists())
                file.createNewFile()

            // Writes chunk data to temp file (*.part)
            i("write to file started")
            writeToFile(file, chunk)
            i("write to file finished")

            if (countMatchingFiles(dirPath, tempFilesRegex) == totalChunks) {
                i("$TAG.handleFileChunk all chunk files created. count=$totalChunks")
                val chunksInClear: MutableList<ByteArray> =
                    getAllChunks(dirPath, totalChunks, areChunksEncrypted = shouldEncryptChunks)
                finalFileContent = mergeFileChunks(chunksInClear)

            } else return
        }

//        i("${file.name} hash before final encryption=" + getFileHash(file))
//        i("${file.name} file size in bytes before final encryption=" + file.length())

        val finalFile = File(dirPath, fileName)
        if (shouldEncrypt) {
            val encryptedFinalFile =
                fileEncryptionManager.encryptByteArray(finalFileContent)

            // saves the final file to the specified output file path.
            writeToFile(finalFile, encryptedFinalFile)
        } else {
            writeToFile(finalFile, finalFileContent)
        }

        logFinalFile(finalFile, shouldEncrypt)

        // delete all chunk files
        deleteFilesMatchingRegex(dirPath, tempFilesRegex)

    }

    /**
     * Validates if all the needed information for creating a file
     * are present in received FCM message.
     */
    private fun isFileMessageValid(data: Map<String, String>): Boolean =
        data.containsKey(FcmKeys.FILE_NAME.toString()) && data[FcmKeys.FILE_NAME.toString()]!!.isNotBlank() &&
                data.containsKey(FcmKeys.FILE_CHUNK_INDEX.toString()) && data[FcmKeys.FILE_CHUNK_INDEX.toString()]!!.isNotBlank() &&
                data.containsKey(FcmKeys.FILE_TOTAL_CHUNKS.toString()) && data[FcmKeys.FILE_TOTAL_CHUNKS.toString()]!!.isNotBlank() &&
                data.containsKey(FcmKeys.FILE_CHUNK_DATA.toString()) && data[FcmKeys.FILE_CHUNK_DATA.toString()]!!.isNotBlank()

    internal fun getAllChunks(
        dirPath: String,
        totalChunks: Int,
        areChunksEncrypted: Boolean
    ): MutableList<ByteArray> {
        i("$TAG.decryptAllChunks dirPath=$dirPath count=$totalChunks isEncrypted=$areChunksEncrypted")
        val fileChunks: MutableList<ByteArray> = mutableListOf()

        for (i in 0 until totalChunks) {
            val chunkFile = File(dirPath, "$i.part").readBytes()

            val chunkContent = if (areChunksEncrypted)
                fileEncryptionManager.decryptByteArray(chunkFile)
            else
                chunkFile

            fileChunks.add(chunkContent)

        }

        return fileChunks
    }


    fun logFinalFile(finalFile: File, shouldEncrypt: Boolean) {
        val fileContentForLogging = finalFile.readBytes()
        // just for logging
        if (shouldEncrypt) {
            val fileContentDecryptedForLogging = fileEncryptionManager.decryptByteArray(
                fileContentForLogging,
            ).decodeToString()
            i("${finalFile.name} fileContentDecrypted.count() = ${fileContentDecryptedForLogging.count()}")
            i("${finalFile.name} hash of decrypted final encryption=" + getFileHash(finalFile))
            i("${finalFile.name} file size seeking in bytes decrypted=" + finalFile.length())
        }

        i("${finalFile.name} hash of created local final file=" + getFileHash(finalFile))
        i("${finalFile.name} size of created local final file=${finalFile.length()} Bytes")
    }

    /**
     * Merges the given file chunks.
     * @param inputChunks An array of byte arrays containing the content of the file chunks to merge.
     * @throws Exception if there is an error merging or saving the file.
     */
    internal fun mergeFileChunks(inputChunks: MutableList<ByteArray>): ByteArray {
        i("$TAG.mergeFileChunks inputChunks=$inputChunks")

        // Merge file chunks into a single byte array
        val mergedFileContent = inputChunks.fold(ByteArray(0)) { acc, byteArray -> acc + byteArray }
        return mergedFileContent
    }

    private fun doesFolderExist(folderName: String): Boolean =
        File(cachedFilesDir, folderName).isDirectory

    companion object {
        private const val TAG = "FileChunkHandler"

    }

}