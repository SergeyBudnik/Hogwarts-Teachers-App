package com.bdev.hengschoolteacher.interactors.auth

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthModule {
    @Singleton @Binds
    abstract fun bindAuthActionsInteractor(
        authActionsInteractor: AuthActionsInteractorImpl
    ): AuthActionsInteractor

    @Singleton @Binds
    abstract fun bindAuthStorageInteractor(
        authStorageInteractor: AuthStorageInteractorImpl
    ): AuthStorageInteractor
}