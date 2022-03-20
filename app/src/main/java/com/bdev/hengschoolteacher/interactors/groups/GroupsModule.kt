package com.bdev.hengschoolteacher.interactors.groups

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class GroupsModule {
    @Singleton @Binds
    abstract fun bindGroupsLoadingInteractor(
        groupsLoadingInteractor: GroupsLoadingInteractorImpl
    ): GroupsLoadingInteractor

    @Singleton @Binds
    abstract fun bindGroupsStorageInteractor(
        groupsStorageInteractor: GroupsStorageInteractorImpl
    ): GroupsStorageInteractor

    @Singleton @Binds
    abstract fun bindGroupsStudentsProviderInteractor(
        groupsStudentsProviderInteractor: GroupsStudentsProviderInteractorImpl
    ): GroupsStudentsProviderInteractor
}