package com.example.courcework.data.network.events

import com.example.courcework.data.network.events.model.StreamEvent
import kotlinx.coroutines.flow.Flow

interface StreamEventHandler {
    fun getEvents(): Flow<List<StreamEvent>>
}