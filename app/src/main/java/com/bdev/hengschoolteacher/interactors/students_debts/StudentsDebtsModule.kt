package com.bdev.hengschoolteacher.interactors.students_debts

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class StudentsDebtsModule {
    @Singleton @Binds
    abstract fun bindStudentsDebtsInteractor(
        studentsDebtsInteractor: StudentsDebtsInteractorImpl
    ): StudentsDebtsInteractor
}