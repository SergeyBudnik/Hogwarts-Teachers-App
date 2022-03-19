package com.bdev.hengschoolteacher.interactors.students_attendances

import com.bdev.hengschoolteacher.async.CommonAsyncService
import com.bdev.hengschoolteacher.async.common.SmartPromise
import com.bdev.hengschoolteacher.async.common.SmartTask.Companion.smartTask
import com.bdev.hengschoolteacher.rest.StudentsAttendancesRest
import com.bdev.hengschoolteacher.interactors.auth.AuthStorageInteractorImpl
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean
import org.androidannotations.rest.spring.annotations.RestService

interface StudentsAttendancesLoadingService {
    fun load(): SmartPromise<Unit, Exception>
}

@EBean(scope = EBean.Scope.Singleton)
open class StudentsAttendancesLoadingServiceImpl : StudentsAttendancesLoadingService, CommonAsyncService() {
    @RestService
    lateinit var studentsAttendancesRest: StudentsAttendancesRest

    @Bean
    lateinit var authService: AuthStorageInteractorImpl

    @Bean
    lateinit var studentsAttendancesStorageService: StudentsAttendancesStorageServiceImpl

    override fun load(): SmartPromise<Unit, Exception> {
        return smartTask {
            authenticateAll(listOf(studentsAttendancesRest), authService.getAuthInfo())

            studentsAttendancesStorageService.setAll(
                    studentsAttendancesRest.getStudentsAttendances()
            )
        }
    }
}