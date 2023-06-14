package com.example.courcework.app.di.bottom

import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Router
import dagger.Module
import dagger.Provides

@Module
object BottomNavigationModule {

    @BottomScope
    @Provides
    fun provideCicerone() = Cicerone.create()

    @BottomScope
    @Provides
    @Bottom
    fun provideRouter(cicerone: Cicerone<Router>) = cicerone.router

    @BottomScope
    @Provides
    @Bottom
    fun provideNavigatorHolder(cicerone: Cicerone<Router>) = cicerone.getNavigatorHolder()
}