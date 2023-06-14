package com.example.courcework.app.di.imageviewer

import com.example.courcework.app.presentation.screens.imageviewer.ImageViewerFragment
import dagger.Component

@ImageViewerScope
@Component(dependencies = [ImageViewerDeps::class], modules = [ImageViewerModule::class])
interface ImageViewerComponent {

    fun inject(fragment: ImageViewerFragment)

    @Component.Factory
    interface Factory {
        fun create(deps: ImageViewerDeps) : ImageViewerComponent
    }
}