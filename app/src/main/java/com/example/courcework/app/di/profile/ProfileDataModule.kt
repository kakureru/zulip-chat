package com.example.courcework.app.di.profile

import com.example.courcework.data.repository.PeopleRepositoryImpl
import com.example.courcework.data.repository.UserRepositoryImpl
import com.example.courcework.domain.repository.PeopleRepository
import com.example.courcework.domain.repository.UserRepository
import dagger.Binds
import dagger.Module

@Module
interface ProfileDataModule {

    @ProfileScope
    @Binds
    fun bindPeopleRepository(impl: PeopleRepositoryImpl): PeopleRepository

    @ProfileScope
    @Binds
    fun bindUserRepository(impl: UserRepositoryImpl): UserRepository
}