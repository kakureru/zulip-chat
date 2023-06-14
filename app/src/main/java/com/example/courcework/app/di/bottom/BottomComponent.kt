package com.example.courcework.app.di.bottom

import com.example.courcework.app.presentation.screens.bottom.BottomFragment
import dagger.Component

@BottomScope
@Component(modules = [BottomNavigationModule::class])
interface BottomComponent {

    fun inject(fragment: BottomFragment)
}