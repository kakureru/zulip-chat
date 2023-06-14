package com.example.courcework.data.network.interceptor

import com.example.courcework.data.network.auth.AuthHelper
import okhttp3.Interceptor
import okhttp3.Response

class AuthHeaderInterceptor(
    private val authHelper: AuthHelper,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain
            .request()
            .newBuilder()
        requestBuilder.addHeader(authHelper.authHeader, authHelper.credentials)
        return chain.proceed(requestBuilder.build())
    }
}