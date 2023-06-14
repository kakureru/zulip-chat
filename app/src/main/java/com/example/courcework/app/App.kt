package com.example.courcework.app

import android.app.Application
import android.content.Context
import com.example.courcework.app.di.app.AppComponent
import com.example.courcework.app.di.app.DaggerAppComponent

class App : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.factory().create(this@App)
    }
}

fun Context.getAppComponent(): AppComponent = (this.applicationContext as App).appComponent