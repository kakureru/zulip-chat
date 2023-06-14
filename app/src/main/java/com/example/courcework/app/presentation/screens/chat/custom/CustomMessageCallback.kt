package com.example.courcework.app.presentation.screens.chat.custom

import com.example.courcework.app.presentation.screens.chat.model.EmojiItem

interface CustomMessageCallback {
    fun onLongClick(messageId: Int)
    fun onLinkClick(url: String)
    fun onReactionClick(emojiItem: EmojiItem, messageId: Int)
    fun onNewReactionClick(messageId: Int)
}