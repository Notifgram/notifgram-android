package com.notifgram.core.data_remote.networking

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.notifgram.core.common.MyLog.e
import com.notifgram.core.common.MyLog.i
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class DownloadReceiver : BroadcastReceiver() {

    @Inject
    @Named("CachedFilesDir")
    lateinit var cachedFilesDir: File

    override fun onReceive(context: Context?, intent: Intent?) {
        i("$TAG onReceive()")
//        GlobalScope.launch {
            if (intent?.action == DownloadManager.ACTION_DOWNLOAD_COMPLETE) {
//                delay(10000)
                val downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
                if (downloadId != -1L && context != null) {
                    i("$TAG Download completed: $downloadId")
                    // Handle download completion, e.g., move the file to internal storage
                    // You can call the method to move the file here.
                    val downloadManager =
                        context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                    val query = DownloadManager.Query().setFilterById(downloadId)
                    val cursor = downloadManager.query(query)
                    if (cursor.moveToFirst()) {
                        val status =
                            cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                        if (status == DownloadManager.STATUS_SUCCESSFUL) {
                            val uri =
                                cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI))
                            i("$TAG uri=$uri")
                            val fileName = Uri.parse(uri).lastPathSegment
                            i("$TAG fileName=$fileName")

                            if (uri != null) {
                                i("Uri.parse(uri).path=${Uri.parse(uri).path}")

                                val path = Uri.parse(uri).path
                                val downloadFile = File(path)

                                val destinationFileDirectory =
                                    cachedFilesDir.path.plus("/").plus(fileName?.replace('.', '-'))
                                i("destinationFileDirectory=$destinationFileDirectory")
                                val internalFile = File(destinationFileDirectory, fileName)
                                i("internalFile.path=${internalFile.path}")
                                if (downloadFile.exists()) {
                                    i("$TAG downloadFile.exists()")
                                    downloadFile.copyTo(internalFile, true)
                                    downloadFile.delete() // Delete the file from external storage after moving
                                    i(
                                        "$TAG File moved to internal storage: ${internalFile.absolutePath}"
                                    )
                                } else {
                                    e("$TAG Downloaded file not found: $uri")
                                }
                            }
                        }
                    }
                    cursor.close()
                }
//            }
        }
    }

    companion object {
        private const val TAG = "DownloadReceiver"
    }
}
