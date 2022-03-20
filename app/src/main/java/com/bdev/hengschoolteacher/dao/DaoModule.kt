package com.bdev.hengschoolteacher.dao

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DaoModule {
    @Singleton @Binds
    abstract fun bindAuthDao(
        authDao: AuthDaoImpl
    ): AuthDao

    @Singleton @Binds
    abstract fun bindGroupsDao(
        groupsDao: GroupsDaoImpl
    ): GroupsDao

    @Singleton @Binds
    abstract fun bindLessonStatusDao(
        lessonStatusDao: LessonStatusDaoImpl
    ): LessonStatusDao

    @Singleton @Binds
    abstract fun bindStaffMembersDao(
        staffMembersDao: StaffMembersDaoImpl
    ): StaffMembersDao

    @Singleton @Binds
    abstract fun bindStudentsAttendancesDao(
        studentsAttendancesDao: StudentsAttendancesDaoImpl
    ): StudentsAttendancesDao

    @Singleton @Binds
    abstract fun bindStudentsDao(
        studentsDao: StudentsDaoImpl
    ): StudentsDao

    @Singleton @Binds
    abstract fun bindStudentsPaymentsDao(
        studentsPaymentsDao: StudentsPaymentsDaoImpl
    ): StudentsPaymentsDao

    @Singleton @Binds
    abstract fun bindUserPreferencesDao(
        userPreferencesDao: UserPreferencesDaoImpl
    ): UserPreferencesDao
}