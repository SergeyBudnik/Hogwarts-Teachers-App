package com.bdev.hengschoolteacher.network.api_provider

import com.bdev.hengschoolteacher.network.api.auth.AuthApiProvider
import com.bdev.hengschoolteacher.network.api.auth.AuthApiProviderImpl
import com.bdev.hengschoolteacher.network.api.groups.GroupsApiProvider
import com.bdev.hengschoolteacher.network.api.groups.GroupsApiProviderImpl
import com.bdev.hengschoolteacher.network.api.lessons_status.LessonsStatusApiProvider
import com.bdev.hengschoolteacher.network.api.lessons_status.LessonsStatusApiProviderImpl
import com.bdev.hengschoolteacher.network.api.staff_members.StaffMembersApiProvider
import com.bdev.hengschoolteacher.network.api.staff_members.StaffMembersApiProviderImpl
import com.bdev.hengschoolteacher.network.api.students.StudentsApiProvider
import com.bdev.hengschoolteacher.network.api.students.StudentsApiProviderImpl
import com.bdev.hengschoolteacher.network.api.students_attendances.StudentsAttendancesApiProvider
import com.bdev.hengschoolteacher.network.api.students_attendances.StudentsAttendancesApiProviderImpl
import com.bdev.hengschoolteacher.network.api.students_payments.StudentsPaymentsApiProvider
import com.bdev.hengschoolteacher.network.api.students_payments.StudentsPaymentsApiProviderImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ApiProviderModule {
    @Singleton @Binds
    abstract fun bindAllApiProvider(
        allApiProvider: AllApiProviderImpl
    ): AllApiProvider

    @Singleton @Binds
    abstract fun bindAuthApiProvider(
        authApiProvider: AuthApiProviderImpl
    ): AuthApiProvider

    @Singleton @Binds
    abstract fun bindGroupsApiProvider(
        groupsApiProvider: GroupsApiProviderImpl
    ): GroupsApiProvider

    @Singleton @Binds
    abstract fun bindLessonsStatusApiProvider(
        lessonsStatusApiProvider: LessonsStatusApiProviderImpl
    ): LessonsStatusApiProvider

    @Singleton @Binds
    abstract fun bindStaffMembersApiProvider(
        staffMembersApiProvider: StaffMembersApiProviderImpl
    ): StaffMembersApiProvider

    @Singleton @Binds
    abstract fun bindStudentsApiProvider(
        studentsApiProvider: StudentsApiProviderImpl
    ): StudentsApiProvider

    @Singleton @Binds
    abstract fun bindStudentsAttendancesApiProvider(
        studentsAttendancesApiProvider: StudentsAttendancesApiProviderImpl
    ): StudentsAttendancesApiProvider

    @Singleton @Binds
    abstract fun bindStudentsPaymentsApiProvider(
        studentsPaymentsApiProvider: StudentsPaymentsApiProviderImpl
    ): StudentsPaymentsApiProvider
}