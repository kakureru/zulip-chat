package com.example.courcework.app.di.attachment

import android.content.ContentResolver
import coil.ImageLoader
import com.example.courcework.app.presentation.downloader.Downloader
import com.example.courcework.data.network.api.FileApi
import com.github.terrakok.cicerone.Router

interface AttachmentDeps {
    fun router(): Router
    fun fileApi(): FileApi
    fun downloader(): Downloader
    fun imageLoader(): ImageLoader
    fun contentResolver(): ContentResolver
}