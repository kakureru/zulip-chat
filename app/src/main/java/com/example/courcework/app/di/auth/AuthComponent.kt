package com.example.courcework.app.di.auth

import com.example.courcework.app.presentation.screens.auth.AuthFragment
import dagger.Component

@Component(dependencies = [AuthDeps::class], modules = [AuthDataModule::class])
interface AuthComponent {

    fun inject(fragment: AuthFragment)

    @Component.Factory
    interface Factory {
        fun create(authDeps: AuthDeps): AuthComponent
    }
}