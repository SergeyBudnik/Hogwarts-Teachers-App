package com.bdev.hengschoolteacher.async

import com.bdev.hengschoolteacher.async.common.SmartPromise
import com.bdev.hengschoolteacher.async.common.SmartTask.Companion.smartTask
import com.bdev.hengschoolteacher.rest.*
import com.bdev.hengschoolteacher.service.*
import com.bdev.hengschoolteacher.service.teacher.TeacherStorageService
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
    lateinit var teachersRest: TeachersRest
    @RestService
    lateinit var lessonStatusRest: LessonStatusRest
    @RestService
    lateinit var usersRequestsRest: UsersRequestsRest
    @RestService
    lateinit var lessonsTransferRest: LessonsTransferRest

    @Bean
    lateinit var authService: AuthService

    @Bean
    lateinit var groupsService: GroupsService
    @Bean
    lateinit var studentsService: StudentsService
    @Bean
    lateinit var studentsAttendancesService: StudentsAttendancesService
    @Bean
    lateinit var studentsPaymentsService: StudentsPaymentsService
    @Bean
    lateinit var teacherStorageService: TeacherStorageService
    @Bean
    lateinit var lessonStatusService: LessonStatusService
    @Bean
    lateinit var usersRequestsService: UsersRequestsService
    @Bean
    lateinit var lessonsTransferStorageService: LessonsTransferStorageService

    fun load(): SmartPromise<Unit, Exception> {
        return smartTask {
            authenticateAll(
                    listOf(lessonStatusRest, usersRequestsRest),
                    authService.getAuthInfo()
            )

            val lessonsStatuses = lessonStatusRest.getAllLessonsStatuses(0, Long.MAX_VALUE) // ToDo
            val usersRequests = usersRequestsRest.getAllUsersRequests()

            lessonStatusService.setLessonsStatuses(lessonsStatuses)
            usersRequestsService.setUsersRequests(usersRequests)
        }
    }

    fun loadStudents(): SmartPromise<Unit, Exception> {
        return smartTask {
            authenticateAll(listOf(studentsRest), authService.getAuthInfo())

            studentsService.setStudents(
                    studentsRest.getAllStudents()
            )
        }
    }

    fun loadGroups(): SmartPromise<Unit, Exception> {
        return smartTask {
            authenticateAll(listOf(groupsRest), authService.getAuthInfo())

            groupsService.setGroups(
                    groupsRest.getAllGroups()
            )
        }
    }

    fun loadTeachers(): SmartPromise<Unit, Exception> {
        return smartTask {
            authenticateAll(listOf(teachersRest), authService.getAuthInfo())

            teacherStorageService.setTeachers(
                    teachersRest.getTeachers()
            )
        }
    }

    fun loadStudentsPayments(): SmartPromise<Unit, Exception> {
        return smartTask {
            authenticateAll(listOf(studentsPaymentsRest), authService.getAuthInfo())

            studentsPaymentsService.setPayments(
                    studentsPaymentsRest.getStudentsPayments()
            )
        }
    }

    fun loadStudentsAttendances(): SmartPromise<Unit, Exception> {
        return smartTask {
            authenticateAll(listOf(studentsAttendancesRest), authService.getAuthInfo())

            studentsAttendancesService.setAttendances(
                    studentsAttendancesRest.getStudentsAttendances()
            )
        }
    }

    fun loadLessonsTransfers(): SmartPromise<Unit, Exception> {
        return smartTask {
            authenticateAll(listOf(lessonsTransferRest), authService.getAuthInfo())

            lessonsTransferStorageService.setLessonsTransfers(
                lessonsTransferRest.getAllLessonsTransfers()
            )
        }
    }
}
