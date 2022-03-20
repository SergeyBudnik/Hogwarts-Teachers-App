package com.bdev.hengschoolteacher.interactors.lessons_status

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LessonsStatusModule {
    @Singleton @Binds
    abstract fun bindLessonsStatusLoadingInteractor(
        lessonsStatusLoadingInteractor: LessonsStatusLoadingInteractorImpl
    ): LessonsStatusLoadingInteractor

    @Singleton @Binds
    abstract fun bindLessonsStatusStorageInteractor(
        lessonsStatusStorageInteractor: LessonsStatusStorageInteractorImpl
    ): LessonsStatusStorageInteractor
}