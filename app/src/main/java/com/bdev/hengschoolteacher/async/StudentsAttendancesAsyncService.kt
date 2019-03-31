package com.bdev.hengschoolteacher.async

import com.bdev.hengschoolteacher.async.common.SmartPromise
import com.bdev.hengschoolteacher.async.common.SmartTask.Companion.smartTask
import com.bdev.hengschoolteacher.data.school.student.StudentAttendance
import com.bdev.hengschoolteacher.rest.StudentsAttendancesRest
import com.bdev.hengschoolteacher.service.AuthService
import com.bdev.hengschoolteacher.service.StudentsAttendancesService
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean
import org.androidannotations.rest.spring.annotations.RestService

@EBean
open class StudentsAttendancesAsyncService : CommonAsyncService() {
    @RestService
    lateinit var studentsAttendancesRest: StudentsAttendancesRest

    @Bean
    lateinit var authService: AuthService

    @Bean
    lateinit var studentsAttendancesService: StudentsAttendancesService

    fun addAttendance(attendance: StudentAttendance): SmartPromise<Unit, Exception> {
        return smartTask {
            authenticateAll(
                    listOf(studentsAttendancesRest),
                    authService.getAuthInfo()
            )

            attendance.id = trustSsl(studentsAttendancesRest).addStudentAttendance(attendance)

            studentsAttendancesService.addAttendance(attendance)
        }
    }
}
