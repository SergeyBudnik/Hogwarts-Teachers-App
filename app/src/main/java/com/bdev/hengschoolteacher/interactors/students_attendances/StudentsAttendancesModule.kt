package com.bdev.hengschoolteacher.interactors.students_attendances

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class StudentsAttendancesModule {
    @Singleton @Binds
    abstract fun bindStudentsAttendancesLoadingInteractor(
        studentsAttendancesLoadingInteractor: StudentsAttendancesLoadingInteractorImpl
    ): StudentsAttendancesLoadingInteractor

    @Singleton @Binds
    abstract fun bindStudentsAttendancesModifierInteractor(
        studentsAttendancesModifierInteractor: StudentsAttendancesModifierInteractorImpl
    ): StudentsAttendancesModifierInteractor

    @Singleton @Binds
    abstract fun bindStudentsAttendancesProviderInteractor(
        studentsAttendancesProviderInteractor: StudentsAttendancesProviderInteractorImpl
    ): StudentsAttendancesProviderInteractor

    @Singleton @Binds
    abstract fun bindStudentsAttendancesStorageInteractor(
        studentsAttendancesStorageInteractor: StudentsAttendancesStorageInteractorImpl
    ): StudentsAttendancesStorageInteractor
}