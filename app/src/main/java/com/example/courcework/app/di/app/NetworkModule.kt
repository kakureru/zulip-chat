package com.example.courcework.app.di.app

import android.app.Application
import coil.ImageLoader
import coil.util.DebugLogger
import com.example.courcework.app.presentation.connectivity.ConnectivityObserver
import com.example.courcework.app.presentation.connectivity.ConnectivityObserverImpl
import com.example.courcework.app.presentation.downloader.Downloader
import com.example.courcework.app.presentation.downloader.DownloaderImpl
import com.example.courcework.data.network.ApiUrlProvider
import com.example.courcework.data.network.api.FileApi
import com.example.courcework.data.network.interceptor.UploadsUrlCompleteInterceptor
import com.example.courcework.data.network.api.MessageApi
import com.example.courcework.data.network.api.StreamApi
import com.example.courcework.data.network.api.UserApi
import com.example.courcework.data.network.api.ZulipApi
import com.example.courcework.data.network.auth.AuthHelper
import com.example.courcework.data.network.interceptor.AuthHeaderInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
object NetworkModule {

    @Singleton
    @Provides
    fun provideApiUrlProvider(): ApiUrlProvider = ApiUrlProvider.Impl()

    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        }

    @Singleton
    @Provides
    fun provideAuthHeaderInterceptor(
        authHelper: AuthHelper,
    ): AuthHeaderInterceptor = AuthHeaderInterceptor(authHelper)

    @Singleton
    @Provides
    fun provideUploadsUrlCompleteInterceptor(
        apiUrlProvider: ApiUrlProvider
    ): UploadsUrlCompleteInterceptor = UploadsUrlCompleteInterceptor(apiUrlProvider)

    @Singleton
    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        apiUrlProvider: ApiUrlProvider
    ): Retrofit = Retrofit
        .Builder()
        .baseUrl(apiUrlProvider.apiUrl)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Singleton
    @Provides
    fun provideHttpClient(
        authHeaderInterceptor: AuthHeaderInterceptor,
        uploadsUrlCompleteInterceptor: UploadsUrlCompleteInterceptor,
        httpLoggingInterceptor: HttpLoggingInterceptor,
    ): OkHttpClient = OkHttpClient
        .Builder()
        .addInterceptor(authHeaderInterceptor)
        .addInterceptor(uploadsUrlCompleteInterceptor)
        .addInterceptor(httpLoggingInterceptor)
        .build()

    @Singleton
    @Provides
    @ImageLoaderQualifier
    fun provideImageLoaderHttpClient(
        authHeaderInterceptor: AuthHeaderInterceptor,
        httpLoggingInterceptor: HttpLoggingInterceptor,
    ): OkHttpClient = OkHttpClient
        .Builder()
        .addInterceptor(authHeaderInterceptor)
        .addInterceptor(httpLoggingInterceptor)
        .build()

    @Singleton
    @Provides
    fun provideImageLoader(
        application: Application,
        @ImageLoaderQualifier okHttpClient: OkHttpClient,
    ): ImageLoader = ImageLoader.Builder(application.applicationContext)
        .okHttpClient(okHttpClient)
        .logger(DebugLogger())
        .build()

    @Singleton
    @Provides
    fun provideDownloader(
        application: Application,
        authHelper: AuthHelper,
    ): Downloader = DownloaderImpl(application.applicationContext, authHelper)

    @Singleton
    @Provides
    fun provideZulipApi(retrofit: Retrofit): ZulipApi = retrofit.create(ZulipApi::class.java)

    @Singleton
    @Provides
    fun provideFileApi(retrofit: Retrofit): FileApi = retrofit.create(FileApi::class.java)

    @Singleton
    @Provides
    fun provideMessageApi(retrofit: Retrofit): MessageApi = retrofit.create(MessageApi::class.java)

    @Singleton
    @Provides
    fun provideStreamApi(retrofit: Retrofit): StreamApi = retrofit.create(StreamApi::class.java)

    @Singleton
    @Provides
    fun provideUserApi(retrofit: Retrofit): UserApi = retrofit.create(UserApi::class.java)

    @Singleton
    @Provides
    fun provideConnectivityObserver(
        application: Application
    ): ConnectivityObserver = ConnectivityObserverImpl(application.applicationContext)
}