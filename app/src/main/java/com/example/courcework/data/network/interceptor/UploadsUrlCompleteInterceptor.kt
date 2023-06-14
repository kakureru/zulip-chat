package com.example.courcework.data.network.interceptor

import com.example.courcework.data.network.ApiUrlProvider
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

class UploadsUrlCompleteInterceptor(
    private val apiUrlProvider: ApiUrlProvider,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)
        val contentType = response.body?.contentType()
        val regex = "\"/?user_uploads".toRegex()
        val newBody = response.body?.string()?.replace(
            regex = regex,
            replacement = "\"${apiUrlProvider.baseUrl}/user_uploads",
        )
        return response
            .newBuilder()
            .body(newBody?.toResponseBody(contentType))
            .build()
    }
}