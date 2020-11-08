package com.bdev.hengschoolteacher.async

import com.bdev.hengschoolteacher.async.common.SmartPromise
import com.bdev.hengschoolteacher.async.common.SmartTask.Companion.smartTask
import com.bdev.hengschoolteacher.rest.*
import com.bdev.hengschoolteacher.services.auth.AuthService
import com.bdev.hengschoolteacher.services.LessonStatusService
import com.bdev.hengschoolteacher.services.groups.GroupsLoadingService
import com.bdev.hengschoolteacher.services.groups.GroupsLoadingServiceImpl
import com.bdev.hengschoolteacher.services.groups.GroupsStorageService
import com.bdev.hengschoolteacher.services.groups.GroupsStorageServiceImpl
import com.bdev.hengschoolteacher.services.students.StudentsStorageService
import com.bdev.hengschoolteacher.services.students.StudentsStorageServiceImpl
import com.bdev.hengschoolteacher.services.students_attendances.StudentsAttendancesProviderService
import com.bdev.hengschoolteacher.services.students_payments.StudentsPaymentsModifierService
import com.bdev.hengschoolteacher.services.students_payments.StudentsPaymentsModifierServiceImpl
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean
import org.androidannotations.rest.spring.annotations.RestService

@EBean
open class SchoolDataAsyncService : CommonAsyncService() {
    @RestService
    lateinit var groupsRest: GroupsRest
    @RestService
    lateinit var studentsRest: StudentsRest
    @RestService
    lateinit var studentsAttendancesRest: StudentsAttendancesRest
    @RestService
    lateinit var studentsPaymentsRest: StudentsPaymentsRest
    @RestService
    lateinit var lessonStatusRest: LessonStatusRest
    @RestService
    lateinit var lessonsTransferRest: LessonsTransferRest

    @Bean
    lateinit var authService: AuthService

    @Bean(GroupsLoadingServiceImpl::class)
    lateinit var groupsLoadingService: GroupsLoadingService
    @Bean(StudentsStorageServiceImpl::class)
    lateinit var studentsStorageService: StudentsStorageService
    @Bean
    lateinit var studentsAttendancesProviderService: StudentsAttendancesProviderService
    @Bean(StudentsPaymentsModifierServiceImpl::class)
    lateinit var studentsPaymentsModifierService: StudentsPaymentsModifierService
    @Bean
    lateinit var lessonStatusService: LessonStatusService

    fun load(): SmartPromise<Unit, Exception> {
        return smartTask {
            authenticateAll(
                    listOf(lessonStatusRest),
                    authService.getAuthInfo()
            )

            val lessonsStatuses = lessonStatusRest.getAllLessonsStatuses(0, Long.MAX_VALUE) // ToDo

            lessonStatusService.setLessonsStatuses(lessonsStatuses)
        }
    }

    fun loadStudents(): SmartPromise<Unit, Exception> {
        return smartTask {
            authenticateAll(listOf(studentsRest), authService.getAuthInfo())

            studentsStorageService.setAll(
                    studentsRest.getAllStudents()
            )
        }
    }

    fun loadGroups(): SmartPromise<Unit, Exception> = groupsLoadingService.load()

    fun loadStudentsPayments(): SmartPromise<Unit, Exception> {
        return smartTask {
            authenticateAll(listOf(studentsPaymentsRest), authService.getAuthInfo())

            studentsPaymentsModifierService.setAll(
                    payments = studentsPaymentsRest.getStudentsPayments()
            )
        }
    }
}
