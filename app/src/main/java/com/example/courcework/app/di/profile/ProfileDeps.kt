package com.example.courcework.app.di.profile

import android.content.SharedPreferences
import com.example.courcework.data.db.dao.PeopleDao
import com.example.courcework.data.network.api.UserApi
import com.example.courcework.data.network.auth.AuthHelper
import com.github.terrakok.cicerone.Router

interface ProfileDeps {
    fun sharedPreferences(): SharedPreferences
    fun authHelper(): AuthHelper
    fun userApi(): UserApi
    fun peopleDao(): PeopleDao
    fun router(): Router
}