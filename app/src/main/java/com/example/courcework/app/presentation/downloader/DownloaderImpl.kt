package com.example.courcework.app.presentation.downloader

import android.app.DownloadManager
import android.content.Context
import android.os.Environment
import android.webkit.MimeTypeMap
import androidx.core.net.toUri
import com.example.courcework.data.network.auth.AuthHelper
import javax.inject.Inject

class DownloaderImpl(
    context: Context,
    private val authHelper: AuthHelper,
) : Downloader {

    private val downloadManager = context.getSystemService(DownloadManager::class.java)

    override fun downloadFile(url: String): Long {
        val fileName = url.substring(url.lastIndexOf("/") + 1)
        val request = DownloadManager.Request(url.toUri())
            .setMimeType(url.mimeType())
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .addRequestHeader(authHelper.authHeader, authHelper.credentials)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
        return downloadManager.enqueue(request)
    }

    private fun String.mimeType(): String? =
        MimeTypeMap.getFileExtensionFromUrl(this)?.let { extension ->
            MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
        }
}