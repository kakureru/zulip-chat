package com.example.courcework.app.presentation.screens.chat.imagegetter

import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.text.Html
import android.widget.TextView
import coil.Coil
import coil.ImageLoader
import coil.request.ImageRequest

open class CoilImageGetter(
    private val textView: TextView,
    private val imageLoader: ImageLoader = Coil.imageLoader(textView.context)
) : Html.ImageGetter {

    override fun getDrawable(source: String): Drawable {

        val drawablePlaceholder = DrawablePlaceHolder()

        imageLoader.enqueue(
            ImageRequest.Builder(textView.context)
                .data(source).apply {
                    target { drawable ->
                        drawablePlaceholder.updateDrawable(drawable)
                        textView.text = textView.text
                    }
                }
                .build()
        )
        return drawablePlaceholder
    }

    private class DrawablePlaceHolder : BitmapDrawable() {

        private var drawable: Drawable? = null

        override fun draw(canvas: Canvas) {
            drawable?.draw(canvas)
        }

        fun updateDrawable(drawable: Drawable) {
            this.drawable = drawable
            val width = drawable.intrinsicWidth
            val height = drawable.intrinsicHeight
            drawable.setBounds(0, 0, width, height)
            setBounds(0, 0, width, height)
        }
    }
}