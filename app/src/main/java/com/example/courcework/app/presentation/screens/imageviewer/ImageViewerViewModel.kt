package com.example.courcework.app.presentation.screens.imageviewer

import androidx.lifecycle.ViewModel
import com.github.terrakok.cicerone.Router
import javax.inject.Inject

class ImageViewerViewModel @Inject constructor(
    private val router: Router,
) : ViewModel() {

    fun accept(event: IVEvent) {
        when (event) {
            IVEvent.BackPressed -> quit()
        }
    }

    private fun quit() {
        router.exit()
    }
}