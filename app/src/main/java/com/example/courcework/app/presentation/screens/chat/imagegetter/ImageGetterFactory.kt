package com.example.courcework.app.presentation.screens.chat.imagegetter

import android.widget.TextView
import coil.ImageLoader
import com.example.courcework.data.network.ApiUrlProvider
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class ImageGetterFactory @AssistedInject constructor(
    @Assisted("textView") private val textView: TextView,
    private val imageLoader: ImageLoader
) {

    fun create() = CoilImageGetter(
        textView = textView,
        imageLoader = imageLoader
    )

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("textView") textView: TextView,
        ): ImageGetterFactory
    }
}