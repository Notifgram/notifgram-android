package com.notifgram.core.data_remote.networking

import android.app.DownloadManager
import android.content.Context
import android.content.IntentFilter
import android.net.Uri
import android.os.Environment
import com.notifgram.core.common.MyLog.i
import com.notifgram.core.data_repository.data_source.remote.FileDownloader

class FileDownloaderImpl(private val context: Context) : FileDownloader {

    private val downloadManager: DownloadManager by lazy {
        context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    }

    private val downloadQueue = mutableListOf<Long>()
    private val downloadReceiver = DownloadReceiver()

    init {
        // Register the BroadcastReceiver to listen for download completion events
        context.registerReceiver(
            downloadReceiver,
            IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        )
    }

    override fun addToQueue(url: String) {
        i("$TAG addToQueue() url=$url")
        // Extract filename from URL
        val fileName = Uri.parse(url).lastPathSegment ?: "file_${System.currentTimeMillis()}"

        val downloadRequest = DownloadManager.Request(Uri.parse(url))
            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setTitle("Downloading file")
            .setDescription("Downloading...")
            .setMimeType("*/*")
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)

        val downloadId = downloadManager.enqueue(downloadRequest)
        downloadQueue.add(downloadId)
    }

    fun downloadFilesFromQueue() {
        for (downloadId in downloadQueue) {
            // Implement logic for handling each download if needed

        }
    }

    fun onDestroy() {
        context.unregisterReceiver(downloadReceiver)
    }

    companion object {
        private const val TAG = "FileDownloaderImpl"
    }
}