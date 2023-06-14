package com.example.courcework.app.presentation.screens.chat.model

data class ReactionItem(
    val emoji: EmojiItem,
    var count: Int,
    val selected: Boolean
)