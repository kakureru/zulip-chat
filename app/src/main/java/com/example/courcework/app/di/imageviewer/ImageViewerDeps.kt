package com.example.courcework.app.di.imageviewer

import coil.ImageLoader
import com.github.terrakok.cicerone.Router

interface ImageViewerDeps {
    fun imageLoader(): ImageLoader
    fun router(): Router
}