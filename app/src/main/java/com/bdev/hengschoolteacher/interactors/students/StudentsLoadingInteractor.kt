package com.bdev.hengschoolteacher.interactors.students

import com.bdev.hengschoolteacher.async.CommonAsyncService
import com.bdev.hengschoolteacher.async.common.SmartPromise
import com.bdev.hengschoolteacher.async.common.SmartTask
import com.bdev.hengschoolteacher.interactors.auth.AuthStorageInteractorImpl
import com.bdev.hengschoolteacher.rest.StudentsRest
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean
import org.androidannotations.rest.spring.annotations.RestService

interface StudentsLoadingInteractor {
    fun load(): SmartPromise<Unit, Exception>
}

@EBean
open class StudentsLoadingInteractorImpl : StudentsLoadingInteractor, CommonAsyncService() {
    @Bean
    lateinit var authService: AuthStorageInteractorImpl

    @Bean(StudentsStorageInteractorImpl::class)
    lateinit var studentsStorageInteractor: StudentsStorageInteractor

    @RestService
    lateinit var studentsRest: StudentsRest

    override fun load(): SmartPromise<Unit, Exception> {
        return SmartTask.smartTask {
            authenticateAll(listOf(studentsRest), authService.getAuthInfo())

            studentsStorageInteractor.setAll(
                studentsRest.getAllStudents()
            )
        }
    }
}