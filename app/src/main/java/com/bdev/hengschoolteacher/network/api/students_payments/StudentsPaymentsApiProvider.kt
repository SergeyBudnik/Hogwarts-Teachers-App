package com.bdev.hengschoolteacher.network.api.students_payments

import com.bdev.hengschoolteacher.network.api_provider.AllApiProviderImpl
import com.bdev.hengschoolteacher.network.api_provider.ApiProvider
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

interface StudentsPaymentsApiProvider : ApiProvider<StudentsPaymentsApi>

@EBean
open class StudentsPaymentsApiProviderImpl : StudentsPaymentsApiProvider {
    @Bean
    lateinit var allApiProvider: AllApiProviderImpl

    override fun provide() = allApiProvider.provideStudentsPaymentsApi()
}