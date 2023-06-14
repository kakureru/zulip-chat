package com.example.courcework.data.network

import android.content.ContentResolver
import android.net.Uri
import okhttp3.MediaType
import okhttp3.RequestBody
import okio.BufferedSink
import okio.source

class InputStreamRequestBody(
    private val uri: Uri,
    private val contentResolver: ContentResolver,
    private val contentType: MediaType? = null,
) : RequestBody() {
    override fun contentType(): MediaType? = contentType
    override fun contentLength(): Long = -1
    override fun writeTo(sink: BufferedSink) {
        contentResolver.openInputStream(uri).use { stream ->
            stream?.source()?.let { sink.writeAll(it) }
        }
    }
}