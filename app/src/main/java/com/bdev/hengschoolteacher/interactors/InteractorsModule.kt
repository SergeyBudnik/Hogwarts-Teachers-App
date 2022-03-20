package com.bdev.hengschoolteacher.interactors

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class InteractorsModule {
    @Singleton @Binds
    abstract fun bindLessonsAttendancesService(
        lessonsAttendancesService: LessonsAttendancesServiceImpl
    ): LessonsAttendancesService

    @Singleton @Binds
    abstract fun bindLessonStateService(
        lessonStateService: LessonStateServiceImpl
    ): LessonStateService
}