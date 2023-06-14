package com.example.courcework.app.di.chat

import android.content.ContentResolver
import coil.ImageLoader
import com.example.courcework.app.presentation.connectivity.ConnectivityObserver
import com.example.courcework.app.presentation.downloader.Downloader
import com.example.courcework.data.db.dao.MessageDao
import com.example.courcework.data.db.dao.StreamDao
import com.example.courcework.data.network.ApiUrlProvider
import com.example.courcework.data.network.api.FileApi
import com.example.courcework.data.network.api.MessageApi
import com.example.courcework.data.network.api.StreamApi
import com.example.courcework.data.network.api.ZulipApi
import com.example.courcework.data.network.auth.AuthHelper
import com.github.terrakok.cicerone.Router

interface ChatDeps {
    fun router(): Router
    fun authHelper(): AuthHelper
    fun messageApi(): MessageApi
    fun streamApi(): StreamApi
    fun zulipApi(): ZulipApi
    fun fileApi(): FileApi
    fun messageDao(): MessageDao
    fun streamDao(): StreamDao
    fun imageLoader(): ImageLoader
    fun apiUrlProvider(): ApiUrlProvider
    fun downloader(): Downloader
    fun contentResolver(): ContentResolver
    fun connectivityObserver(): ConnectivityObserver
}