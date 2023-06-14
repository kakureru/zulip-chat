package com.example.courcework.app.di.people

import com.example.courcework.data.repository.PeopleRepositoryImpl
import com.example.courcework.domain.repository.PeopleRepository
import dagger.Binds
import dagger.Module

@Module
interface PeopleDataModule {

    @PeopleScope
    @Binds
    fun bindPeopleRepository(impl: PeopleRepositoryImpl): PeopleRepository
}