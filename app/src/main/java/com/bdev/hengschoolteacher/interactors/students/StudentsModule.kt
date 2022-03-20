package com.bdev.hengschoolteacher.interactors.students

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class StudentsModule {
    @Singleton @Binds
    abstract fun bindStudentsStorageInteractor(
        studentsStorageInteractor: StudentsStorageInteractorImpl
    ): StudentsStorageInteractor

    @Singleton @Binds
    abstract fun bindStudentsLoadingInteractor(
        studentsLoadingInteractor: StudentsLoadingInteractorImpl
    ): StudentsLoadingInteractor
}