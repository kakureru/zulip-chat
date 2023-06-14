package com.example.courcework.app.presentation.screens.channels.stream.model

import android.os.Parcelable
import com.example.courcework.domain.model.Topic
import kotlinx.parcelize.Parcelize

@Parcelize
data class TopicItem(
    val id: TopicId,
    val name: String,
    val unreadMessageCount: Int?,
    val isUnreadCountVisible: Boolean,
) : Parcelable

fun Topic.toUI() = TopicItem(
    id = id.toUI(),
    name = name,
    unreadMessageCount = unreadMessageCount,
    isUnreadCountVisible = unreadMessageCount != null && unreadMessageCount != 0
)