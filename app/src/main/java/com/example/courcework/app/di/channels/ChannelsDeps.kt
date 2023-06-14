package com.example.courcework.app.di.channels

import com.example.courcework.app.presentation.connectivity.ConnectivityObserver

interface ChannelsDeps {
    fun connectivityObserver(): ConnectivityObserver
}
