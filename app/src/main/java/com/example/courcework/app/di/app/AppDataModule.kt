package com.example.courcework.app.di.app

import android.app.Application
import android.content.ContentResolver
import android.content.SharedPreferences
import androidx.room.Room
import com.example.courcework.data.db.AppDatabase
import com.example.courcework.data.db.dao.MessageDao
import com.example.courcework.data.db.dao.PeopleDao
import com.example.courcework.data.db.dao.StreamDao
import com.example.courcework.data.db.dao.UserDao
import com.example.courcework.data.network.auth.AuthHelper
import com.example.courcework.data.network.auth.AuthHelperImpl
import com.example.courcework.data.network.auth.AuthHelperImpl.Companion.SHARED_PREFERENCES_NAME
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [AppDataModule.BindsModule::class])
object AppDataModule {

    @Provides
    fun provideSharedPrefs(application: Application): SharedPreferences =
        application.getSharedPreferences(
            SHARED_PREFERENCES_NAME,
            android.content.Context.MODE_PRIVATE
        )

    @Singleton
    @Provides
    fun provideDatabase(application: Application): AppDatabase = Room.databaseBuilder(
        application,
        AppDatabase::class.java,
        "app_database"
    )
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    fun provideContentResolver(application: Application): ContentResolver = application.contentResolver

    @Singleton
    @Provides
    fun provideMessageDao(database: AppDatabase): MessageDao = database.messageDao()

    @Singleton
    @Provides
    fun providePeopleDao(database: AppDatabase): PeopleDao = database.peopleDao()

    @Singleton
    @Provides
    fun provideStreamDao(database: AppDatabase): StreamDao = database.streamDao()

    @Singleton
    @Provides
    fun provideUserDao(database: AppDatabase): UserDao = database.userDao()

    @Module
    interface BindsModule {

        @Binds
        fun bindAuthHelper(impl: AuthHelperImpl): AuthHelper
    }
}