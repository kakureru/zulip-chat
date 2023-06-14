package com.example.courcework.app.di.people

import com.example.courcework.domain.repository.PeopleRepository
import com.example.courcework.domain.usecase.*
import dagger.Module
import dagger.Provides

@Module
object PeopleDomainModule {

    @Provides
    fun provideGetContactSearchResultsUseCase(
        getContactsUseCase: GetContactsUseCase
    ): GetContactSearchResultsUseCase = GetContactSearchResultsUseCase(getContactsUseCase)

    @Provides
    fun provideGetContactsUseCase(
        peopleRepository: PeopleRepository
    ): GetContactsUseCase = GetContactsUseCase(peopleRepository)
}