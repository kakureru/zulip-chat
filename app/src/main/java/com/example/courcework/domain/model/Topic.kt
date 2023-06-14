package com.example.courcework.domain.model

import com.example.courcework.data.db.model.TopicEntity
import com.example.courcework.data.network.model.streams.TopicDto

data class Topic(
    val id: TopicId,
    val name: String,
    val unreadMessageCount: Int? = null,
)

fun TopicDto.toDomain(streamId: Int): Topic = Topic(
    id = TopicId(streamId, name),
    name = name
)

fun TopicEntity.toDomain(): Topic = Topic(
    id = id,
    name = name
)