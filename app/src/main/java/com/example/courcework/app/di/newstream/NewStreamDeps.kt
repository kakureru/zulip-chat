package com.example.courcework.app.di.newstream

import com.example.courcework.data.db.dao.StreamDao
import com.example.courcework.data.network.api.StreamApi
import com.example.courcework.data.network.api.ZulipApi

interface NewStreamDeps {
    fun streamDao(): StreamDao
    fun streamApi(): StreamApi
    fun zulipApi(): ZulipApi
}