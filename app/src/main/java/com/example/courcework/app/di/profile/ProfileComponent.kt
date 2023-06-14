package com.example.courcework.app.di.profile

import com.example.courcework.app.presentation.screens.profile.ProfileFragment
import dagger.Component

@ProfileScope
@Component(dependencies = [ProfileDeps::class], modules = [ProfileDataModule::class])
interface ProfileComponent {

    fun inject(fragment: ProfileFragment)

    @Component.Factory
    interface Factory {
        fun create(profileDeps: ProfileDeps): ProfileComponent
    }
}