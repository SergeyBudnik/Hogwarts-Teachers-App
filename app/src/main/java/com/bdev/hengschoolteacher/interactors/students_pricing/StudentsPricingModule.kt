package com.bdev.hengschoolteacher.interactors.students_pricing

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class StudentsPricingModule {
    @Singleton @Binds
    abstract fun bindStudentsPricingInteractor(
        studentsPricingInteractor: StudentsPricingInteractorImpl
    ): StudentsPricingInteractor
}