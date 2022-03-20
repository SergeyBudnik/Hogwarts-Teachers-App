package com.bdev.hengschoolteacher.interactors.students_payments

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class StudentsPaymentsModule {
    @Singleton @Binds
    abstract fun bindStudentsPaymentsActionsInteractor(
        studentsPaymentsActionsInteractor: StudentsPaymentsActionsInteractorImpl
    ): StudentsPaymentsActionsInteractor

    @Singleton @Binds
    abstract fun bindStudentsPaymentsLoadingInteractor(
        studentsPaymentsLoadingInteractor: StudentsPaymentsLoadingInteractorImpl
    ): StudentsPaymentsLoadingInteractor

    @Singleton @Binds
    abstract fun bindStudentsPaymentsModifierInteractor(
        studentsPaymentsModifierInteractor: StudentsPaymentsModifierInteractorImpl
    ): StudentsPaymentsModifierInteractor

    @Singleton @Binds
    abstract fun bindStudentsPaymentsProviderInteractor(
        studentsPaymentsProviderInteractor: StudentsPaymentsProviderInteractorImpl
    ): StudentsPaymentsProviderInteractor

    @Singleton @Binds
    abstract fun bindStudentsPaymentsStorageInteractor(
        studentsPaymentsStorageInteractor: StudentsPaymentsStorageInteractorImpl
    ): StudentsPaymentsStorageInteractor
}