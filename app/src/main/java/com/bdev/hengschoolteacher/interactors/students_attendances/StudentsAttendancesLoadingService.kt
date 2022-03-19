package com.bdev.hengschoolteacher.interactors.students_attendances

import com.bdev.hengschoolteacher.async.common.SmartPromise
import com.bdev.hengschoolteacher.async.common.SmartTask.Companion.smartTask
import com.bdev.hengschoolteacher.interactors.auth.AuthStorageInteractorImpl
import com.bdev.hengschoolteacher.network.api.students_attendances.StudentsAttendancesApiProviderImpl
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

interface StudentsAttendancesLoadingService {
    fun load(): SmartPromise<Unit, Exception>
}

@EBean(scope = EBean.Scope.Singleton)
open class StudentsAttendancesLoadingServiceImpl : StudentsAttendancesLoadingService {
    @Bean
    lateinit var studentsAttendancesApiProvider: StudentsAttendancesApiProviderImpl

    @Bean
    lateinit var authService: AuthStorageInteractorImpl

    @Bean
    lateinit var studentsAttendancesStorageService: StudentsAttendancesStorageServiceImpl

    override fun load(): SmartPromise<Unit, Exception> {
        return smartTask {
            studentsAttendancesStorageService.setAll(
                    studentsAttendancesApiProvider.provide().getStudentsAttendances().execute().body()!!
            )
        }
    }
}