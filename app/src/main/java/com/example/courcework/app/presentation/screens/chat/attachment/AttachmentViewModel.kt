package com.example.courcework.app.presentation.screens.chat.attachment

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.courcework.app.presentation.runCatchingNonCancellation
import com.example.courcework.domain.repository.FileRepository
import com.github.terrakok.cicerone.Router
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class AttachmentViewModel @Inject constructor(
    private val uri: Uri,
    private val mimeType: String?,
    private val resultKey: String,
    private val router: Router,
    private val fileRepository: FileRepository,
): ViewModel() {

    private val _effect = MutableSharedFlow<AttachmentEffect>()
    val effect: SharedFlow<AttachmentEffect> = _effect.asSharedFlow()

    fun accept(event: AttachmentEvent) {
        when (event) {
            AttachmentEvent.BackPressed -> quit()
            is AttachmentEvent.SendMessage -> sendMessage(event.text)
        }
    }

    private fun sendMessage(text: String) = viewModelScope.launch {
        runCatchingNonCancellation {
            val fileName = uri.path.constructFileName(mimeType)
            val attachmentLink = fileRepository.uploadFile(uri.toString(), fileName)
            val result = AttachmentResult(
                message = text,
                link = attachmentLink,
                fileName = fileName,
            )
            router.sendResult(resultKey, result)
            router.exit()
        }.onFailure {
            it.printStackTrace()
            _effect.emit(AttachmentEffect.Error("Failed to send message"))
        }
    }

    private fun String?.constructFileName(mimeType: String?): String {
        val name = this?.lastSegment() ?: throw IllegalArgumentException()
        return if (name.extension() != null) name
        else name + '.' + (mimeType?.lastSegment() ?: throw IllegalArgumentException())
    }

    private fun String.lastSegment(): String? = lastIndexOf('/').let {
        if (it == -1) null
        else substring( it + 1)
    }

    private fun String.extension(): String? = lastIndexOf('.').let {
        if (it == -1) null
        else ".${substring( it + 1)}"
    }

    private fun quit() {
        router.exit()
    }
}

class AttachmentViewModelFactory @AssistedInject constructor(
    @Assisted("uri") private val uri: Uri,
    @Assisted("mimeType") private val mimeType: String?,
    @Assisted("resultKey") private val resultKey: String,
    private val router: Router,
    private val fileRepository: FileRepository,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AttachmentViewModel::class.java))
            return AttachmentViewModel(uri, mimeType, resultKey, router, fileRepository) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("uri") uri: Uri,
            @Assisted("mimeType") mimeType: String?,
            @Assisted("resultKey") resultKey: String
        ): AttachmentViewModelFactory
    }
}