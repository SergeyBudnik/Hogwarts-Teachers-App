package com.bdev.hengschoolteacher.interactors.user_preferences

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UserPreferencesModule {
    @Singleton @Binds
    abstract fun bindUserPreferencesInteractor(
        userPreferencesInteractor: UserPreferencesInteractorImpl
    ): UserPreferencesInteractor
}