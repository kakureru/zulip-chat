package com.example.courcework.app.presentation.screens.chat.model

interface MessageItem {
    val id: Int
    val text: String
    val date: String
    val time: String
    val reactions: List<ReactionItem>
}