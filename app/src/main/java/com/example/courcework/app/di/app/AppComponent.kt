package com.example.courcework.app.di.app

import android.app.Application
import android.content.ContentResolver
import android.content.SharedPreferences
import coil.ImageLoader
import com.example.courcework.app.presentation.connectivity.ConnectivityObserver
import com.example.courcework.app.di.attachment.AttachmentDeps
import com.example.courcework.app.di.auth.AuthDeps
import com.example.courcework.app.di.channels.ChannelsDeps
import com.example.courcework.app.di.chat.ChatDeps
import com.example.courcework.app.di.imageviewer.ImageViewerDeps
import com.example.courcework.app.di.newstream.NewStreamDeps
import com.example.courcework.app.di.people.PeopleDeps
import com.example.courcework.app.di.profile.ProfileDeps
import com.example.courcework.app.di.stream.StreamDeps
import com.example.courcework.app.presentation.downloader.Downloader
import com.example.courcework.app.presentation.MainActivity
import com.example.courcework.data.db.dao.MessageDao
import com.example.courcework.data.db.dao.PeopleDao
import com.example.courcework.data.db.dao.StreamDao
import com.example.courcework.data.network.ApiUrlProvider
import com.example.courcework.data.network.api.FileApi
import com.example.courcework.data.network.api.MessageApi
import com.example.courcework.data.network.api.StreamApi
import com.example.courcework.data.network.api.UserApi
import com.example.courcework.data.network.api.ZulipApi
import com.example.courcework.data.network.auth.AuthHelper
import com.github.terrakok.cicerone.Router
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppDataModule::class, NavigationModule::class, NetworkModule::class])
interface AppComponent : AuthDeps, ProfileDeps, ChatDeps, PeopleDeps, ChannelsDeps, StreamDeps,
    NewStreamDeps, ImageViewerDeps, AttachmentDeps {

    fun inject(activity: MainActivity)

    override fun apiUrlProvider(): ApiUrlProvider

    override fun authHelper(): AuthHelper
    override fun imageLoader(): ImageLoader
    override fun downloader(): Downloader
    override fun connectivityObserver(): ConnectivityObserver
    override fun contentResolver(): ContentResolver

    override fun peopleDao(): PeopleDao
    override fun messageDao(): MessageDao
    override fun streamDao(): StreamDao

    override fun router(): Router
    override fun sharedPreferences(): SharedPreferences
    override fun zulipApi(): ZulipApi
    override fun fileApi(): FileApi
    override fun streamApi(): StreamApi
    override fun userApi(): UserApi
    override fun messageApi(): MessageApi

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: Application): AppComponent
    }
}