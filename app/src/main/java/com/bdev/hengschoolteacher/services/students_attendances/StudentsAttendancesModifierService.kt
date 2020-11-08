package com.bdev.hengschoolteacher.services.students_attendances

import com.bdev.hengschoolteacher.async.CommonAsyncService
import com.bdev.hengschoolteacher.async.common.SmartPromise
import com.bdev.hengschoolteacher.async.common.SmartTask.Companion.smartTask
import com.bdev.hengschoolteacher.data.school.student.StudentAttendance
import com.bdev.hengschoolteacher.rest.StudentsAttendancesRest
import com.bdev.hengschoolteacher.services.auth.AuthService
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean
import org.androidannotations.rest.spring.annotations.RestService

@EBean(scope = EBean.Scope.Singleton)
open class StudentsAttendancesModifierService : CommonAsyncService() {
    @RestService
    lateinit var studentsAttendancesRest: StudentsAttendancesRest

    @Bean
    lateinit var authService: AuthService

    @Bean
    lateinit var studentsAttendancesStorageService: StudentsAttendancesStorageService

    fun addAttendance(attendance: StudentAttendance): SmartPromise<Unit, Exception> {
        return smartTask {
            authenticateAll(
                    listOf(studentsAttendancesRest),
                    authService.getAuthInfo()
            )

            studentsAttendancesRest.addStudentAttendance(attendance = attendance)

            studentsAttendancesStorageService.add(studentAttendance = attendance)
        }
    }
}