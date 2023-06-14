package com.example.courcework.app.di.auth

import android.content.SharedPreferences
import com.example.courcework.data.network.api.UserApi
import com.example.courcework.data.network.auth.AuthHelper
import com.github.terrakok.cicerone.Router

interface AuthDeps {
    fun router(): Router
    fun sharedPreferences(): SharedPreferences
    fun userApi(): UserApi
    fun authHelper(): AuthHelper
}