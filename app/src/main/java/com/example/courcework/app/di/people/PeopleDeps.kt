package com.example.courcework.app.di.people

import com.example.courcework.app.presentation.connectivity.ConnectivityObserver
import com.example.courcework.data.db.dao.PeopleDao
import com.example.courcework.data.network.api.UserApi
import com.github.terrakok.cicerone.Router

interface PeopleDeps {
    fun router(): Router
    fun userApi(): UserApi
    fun peopleDao(): PeopleDao
    fun connectivityObserver(): ConnectivityObserver
}