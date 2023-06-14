package com.example.courcework.app.presentation.screens.chat.dialog.reaction

import com.example.courcework.app.presentation.screens.chat.model.EmojiItem

data class ReactionDialogData(
    val emojiSet: List<EmojiItem>,
    val messageId: Int
)