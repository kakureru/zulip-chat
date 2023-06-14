package com.example.courcework.data.repository

import android.content.ContentResolver
import androidx.core.net.toUri
import com.example.courcework.app.presentation.downloader.Downloader
import com.example.courcework.data.network.InputStreamRequestBody
import com.example.courcework.data.network.api.FileApi
import com.example.courcework.domain.repository.FileRepository
import okhttp3.MultipartBody
import javax.inject.Inject

class FileRepositoryImpl @Inject constructor(
    private val fileApi: FileApi,
    private val contentResolver: ContentResolver,
    private val downloader: Downloader,
) : FileRepository {

    override suspend fun uploadFile(uri: String, fileName: String): String =
        fileApi.uploadFile(
            file = MultipartBody.Part.createFormData(
                "file",
                fileName,
                InputStreamRequestBody(uri.toUri(), contentResolver)
            )
        ).uri

    override fun downloadFile(url: String) {
        downloader.downloadFile(url)
    }
}