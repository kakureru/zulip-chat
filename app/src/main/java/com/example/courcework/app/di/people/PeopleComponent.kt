package com.example.courcework.app.di.people

import com.example.courcework.app.presentation.screens.people.PeopleFragment
import dagger.Component

@PeopleScope
@Component(dependencies = [PeopleDeps::class], modules = [PeopleDataModule::class, PeopleDomainModule::class])
interface PeopleComponent {

    fun inject(fragment: PeopleFragment)

    @Component.Factory
    interface Factory {
        fun create(peopleDeps: PeopleDeps): PeopleComponent
    }
}