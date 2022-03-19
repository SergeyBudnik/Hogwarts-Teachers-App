package com.bdev.hengschoolteacher.interactors.students_payments

import com.bdev.hengschoolteacher.async.common.SmartPromise
import com.bdev.hengschoolteacher.async.common.SmartTask
import com.bdev.hengschoolteacher.interactors.auth.AuthStorageInteractorImpl
import com.bdev.hengschoolteacher.network.api.students_payments.StudentsPaymentsApiProviderImpl
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

interface StudentsPaymentsLoadingInteractor {
    fun load(): SmartPromise<Unit, Exception>
}

@EBean
open class StudentsPaymentsLoadingInteractorImpl : StudentsPaymentsLoadingInteractor {
    @Bean
    lateinit var studentsPaymentsApiProvider: StudentsPaymentsApiProviderImpl

    @Bean
    lateinit var authService: AuthStorageInteractorImpl

    @Bean(StudentsPaymentsModifierServiceImpl::class)
    lateinit var studentsPaymentsModifierService: StudentsPaymentsModifierService

    override fun load(): SmartPromise<Unit, Exception> {
        return SmartTask.smartTask {
            studentsPaymentsModifierService.setAll(
                payments = studentsPaymentsApiProvider.provide().getStudentsPayments().execute().body()!!
            )
        }
    }
}