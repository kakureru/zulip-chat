package com.example.courcework.domain.model

import com.example.courcework.data.db.model.StreamEntity
import com.example.courcework.data.db.model.SubscribedStreamEntity
import com.example.courcework.data.network.model.streams.StreamDto

data class Stream (
    val id: Int,
    val name: String,
    val topics: List<Topic>,
)

fun StreamDto.toDomain(): Stream = Stream(
    id = id,
    name = name,
    topics = emptyList(),
)

fun StreamEntity.toDomain(): Stream = Stream(
    id = id,
    name = name,
    topics = emptyList(),
)

fun SubscribedStreamEntity.toDomain(): Stream = Stream(
    id = id,
    name = name,
    topics = emptyList(),
)