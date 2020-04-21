package com.bdev.hengschoolteacher.service.student_attendance

import com.bdev.hengschoolteacher.async.CommonAsyncService
import com.bdev.hengschoolteacher.async.common.SmartPromise
import com.bdev.hengschoolteacher.async.common.SmartTask.Companion.smartTask
import com.bdev.hengschoolteacher.rest.StudentsAttendancesRest
import com.bdev.hengschoolteacher.service.AuthService
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean
import org.androidannotations.rest.spring.annotations.RestService

@EBean(scope = EBean.Scope.Singleton)
open class StudentsAttendancesLoadingService : CommonAsyncService() {
    @RestService
    lateinit var studentsAttendancesRest: StudentsAttendancesRest

    @Bean
    lateinit var authService: AuthService

    @Bean
    lateinit var studentsAttendancesStorageService: StudentsAttendancesStorageService

    fun load(): SmartPromise<Unit, Exception> {
        return smartTask {
            authenticateAll(listOf(studentsAttendancesRest), authService.getAuthInfo())

            studentsAttendancesStorageService.setAll(
                    studentsAttendancesRest.getStudentsAttendances()
            )
        }
    }
}