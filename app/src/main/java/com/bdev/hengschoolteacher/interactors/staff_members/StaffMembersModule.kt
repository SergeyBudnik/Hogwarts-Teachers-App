package com.bdev.hengschoolteacher.interactors.staff_members

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class StaffMembersModule {
    @Singleton @Binds
    abstract fun bindStaffMembersLoadingInteractor(
        staffMembersLoadingInteractor: StaffMembersLoadingInteractorImpl
    ): StaffMembersLoadingInteractor

    @Singleton @Binds
    abstract fun bindStaffMembersStorageInteractor(
        staffMembersStorageInteractor: StaffMembersStorageInteractorImpl
    ): StaffMembersStorageInteractor
}