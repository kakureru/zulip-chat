package com.example.courcework.app.presentation.screens.chat.attachment

sealed class AttachmentEvent {
    class SendMessage(val text: String) : AttachmentEvent()
    object BackPressed : AttachmentEvent()
}

sealed class AttachmentEffect {
    class Error(val msg: String) : AttachmentEffect()
}