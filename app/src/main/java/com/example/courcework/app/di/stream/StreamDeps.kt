package com.example.courcework.app.di.stream

import com.example.courcework.data.db.dao.MessageDao
import com.example.courcework.data.db.dao.StreamDao
import com.example.courcework.data.network.api.MessageApi
import com.example.courcework.data.network.api.StreamApi
import com.example.courcework.data.network.api.ZulipApi
import com.github.terrakok.cicerone.Router

interface StreamDeps {
    fun router(): Router
    fun zulipApi(): ZulipApi
    fun streamApi(): StreamApi
    fun messageApi(): MessageApi
    fun streamDao(): StreamDao
    fun messageDao(): MessageDao
}