package com.bdev.hengschoolteacher.interactors.lessons

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LessonsModule {
    @Singleton @Binds
    abstract fun bindLessonsInteractor(
        lessonsInteractor: LessonsInteractorImpl
    ): LessonsInteractor
}