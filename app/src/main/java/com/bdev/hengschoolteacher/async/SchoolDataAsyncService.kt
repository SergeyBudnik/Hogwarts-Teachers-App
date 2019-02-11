package com.bdev.hengschoolteacher.async

import com.bdev.hengschoolteacher.async.common.SmartPromise
import com.bdev.hengschoolteacher.async.common.SmartTask.Companion.smartTask
import com.bdev.hengschoolteacher.rest.*
import com.bdev.hengschoolteacher.service.*
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
    lateinit var teachersService: TeachersService
    @Bean
    lateinit var lessonStatusService: LessonStatusService
    @Bean
    lateinit var usersRequestsService: UsersRequestsService

    fun load(): SmartPromise<Unit, Exception> {
        return smartTask {
            authenticateAll(
                    listOf(groupsRest, studentsRest, studentsAttendancesRest, studentsPaymentsRest, teachersRest, lessonStatusRest, usersRequestsRest),
                    authService.getAuthInfo()
            )

            val groups = groupsRest.getAllGroups()
            val students = studentsRest.getAllStudents()
            val attendances = studentsAttendancesRest.getStudentsAttendances()
            val payments = studentsPaymentsRest.getStudentsPayments()
            val teachers = teachersRest.getTeachers()
            val lessonsStatuses = lessonStatusRest.getAllLessonsStatuses(0, Long.MAX_VALUE) // ToDo
            val usersRequests = usersRequestsRest.getAllUsersRequests()

            groupsService.setGroups(groups)
            studentsService.setStudents(students)
            studentsAttendancesService.setAttendances(attendances)
            studentsPaymentsService.setPayments(payments)
            teachersService.setTeachers(teachers)
            lessonStatusService.setLessonsStatuses(lessonsStatuses)
            usersRequestsService.setUsersRequests(usersRequests)
        }
    }
}
