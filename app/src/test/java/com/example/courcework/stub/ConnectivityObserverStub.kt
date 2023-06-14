package com.example.courcework.stub

import com.example.courcework.app.presentation.connectivity.ConnectivityObserver
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

class ConnectivityObserverStub : ConnectivityObserver {
    override fun observe(): Flow<ConnectivityObserver.Status> {
        return emptyFlow()
    }
}