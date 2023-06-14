package com.example.courcework.app.di.auth

import com.example.courcework.data.repository.UserRepositoryImpl
import com.example.courcework.domain.repository.UserRepository
import dagger.Binds
import dagger.Module

@Module
interface AuthDataModule {

    @Binds
    fun bindUserRepository(impl: UserRepositoryImpl): UserRepository
}