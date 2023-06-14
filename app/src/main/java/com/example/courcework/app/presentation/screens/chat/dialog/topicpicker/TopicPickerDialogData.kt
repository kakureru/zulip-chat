package com.example.courcework.app.presentation.screens.chat.dialog.topicpicker

import com.example.courcework.app.presentation.screens.channels.stream.model.TopicId


data class TopicPickerDialogData(
    val messageId: Int,
    val currentTopicId: TopicId
)