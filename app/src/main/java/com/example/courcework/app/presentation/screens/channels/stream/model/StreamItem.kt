package com.example.courcework.app.presentation.screens.channels.stream.model

import com.example.courcework.domain.model.Stream

data class StreamItem(
    val id: Int,
    val name: String,
    var expanded: Boolean = false
)

fun Stream.toUI() = StreamItem(
    id = id,
    name = "#$name"
)