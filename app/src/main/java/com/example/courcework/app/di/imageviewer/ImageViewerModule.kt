package com.example.courcework.app.di.imageviewer

import androidx.lifecycle.ViewModel
import com.example.courcework.app.di.ViewModelKey
import com.example.courcework.app.presentation.screens.imageviewer.ImageViewerViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ImageViewerModule {

    @ImageViewerScope
    @Binds
    @IntoMap
    @ViewModelKey(ImageViewerViewModel::class)
    fun bindsImageViewerViewModel(viewModel: ImageViewerViewModel): ViewModel
}