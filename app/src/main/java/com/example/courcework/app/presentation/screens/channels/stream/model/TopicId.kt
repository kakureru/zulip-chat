package com.example.courcework.app.presentation.screens.channels.stream.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TopicId(
    val streamId: Int,
    val topicName: String
) : Parcelable

fun com.example.courcework.domain.model.TopicId.toUI() = TopicId(streamId, topicName)

fun TopicId.toDomain() = com.example.courcework.domain.model.TopicId(streamId, topicName)