package com.bdev.hengschoolteacher.interactors.students_payments

import com.bdev.hengschoolteacher.async.CommonAsyncService
import com.bdev.hengschoolteacher.async.common.SmartPromise
import com.bdev.hengschoolteacher.async.common.SmartTask
import com.bdev.hengschoolteacher.interactors.auth.AuthStorageInteractorImpl
import com.bdev.hengschoolteacher.rest.StudentsPaymentsRest
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean
import org.androidannotations.rest.spring.annotations.RestService

interface StudentsPaymentsLoadingInteractor {
    fun load(): SmartPromise<Unit, Exception>
}

@EBean
open class StudentsPaymentsLoadingInteractorImpl : StudentsPaymentsLoadingInteractor, CommonAsyncService() {
    @RestService
    lateinit var studentsPaymentsRest: StudentsPaymentsRest

    @Bean
    lateinit var authService: AuthStorageInteractorImpl

    @Bean(StudentsPaymentsModifierServiceImpl::class)
    lateinit var studentsPaymentsModifierService: StudentsPaymentsModifierService

    override fun load(): SmartPromise<Unit, Exception> {
        return SmartTask.smartTask {
            authenticateAll(listOf(studentsPaymentsRest), authService.getAuthInfo())

            studentsPaymentsModifierService.setAll(
                payments = studentsPaymentsRest.getStudentsPayments()
            )
        }
    }
}