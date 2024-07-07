package com.notifgram.core.data_repository

import com.notifgram.core.common.MyLog.i
import java.io.File
import javax.inject.Inject
import javax.inject.Named

class FileUriBuilder @Inject constructor(
    @Named("CachedFilesDir") private val cachedFilesDir: File
) {

    fun buildFile(fileName: String?): File? {
        i("convertUriToFile() fileName=$fileName")
        if (fileName == null || fileName == "")
            return null

        val dirPath = cachedFilesDir.path.plus("/")
            .plus(fileName.replace('.', '-')) // files/cached/file-format/
        i(" convertToFile() fileName=$fileName dirPath=$dirPath")
        return File(dirPath, fileName)
    }
}