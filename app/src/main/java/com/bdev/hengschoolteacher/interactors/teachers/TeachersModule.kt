package com.bdev.hengschoolteacher.interactors.teachers

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class TeachersModule {
    @Singleton @Binds
    abstract fun bindTeacherActionsInteractor(
        teacherActionsInteractor: TeacherActionsInteractorImpl
    ): TeacherActionsInteractor

    @Singleton @Binds
    abstract fun bindTeacherInfoInteractor(
        teacherInfoInteractor: TeacherInfoInteractorImpl
    ): TeacherInfoInteractor

    @Singleton @Binds
    abstract fun bindTeacherSalaryInteractor(
        teacherSalaryInteractor: TeacherSalaryInteractorImpl
    ): TeacherSalaryInteractor
}