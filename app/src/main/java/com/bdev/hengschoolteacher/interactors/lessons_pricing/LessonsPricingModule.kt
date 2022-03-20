package com.bdev.hengschoolteacher.interactors.lessons_pricing

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LessonsPricingModule {
    @Singleton @Binds
    abstract fun bindLessonsPricingInteractor(
        lessonsPricingInteractor: LessonsPricingInteractorImpl
    ): LessonsPricingInteractor
}