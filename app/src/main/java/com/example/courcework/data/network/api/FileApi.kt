package com.example.courcework.data.network.api

import com.example.courcework.data.network.model.messages.AttachmentResponse
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface FileApi {

    @Multipart
    @POST("user_uploads")
    suspend fun uploadFile(
        @Part file: MultipartBody.Part
    ): AttachmentResponse
}