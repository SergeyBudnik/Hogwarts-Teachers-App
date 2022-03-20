package com.bdev.hengschoolteacher.network.api.students_payments

import com.bdev.hengschoolteacher.network.api_provider.AllApiProviderImpl
import com.bdev.hengschoolteacher.network.api_provider.ApiProvider
import javax.inject.Inject

interface StudentsPaymentsApiProvider : ApiProvider<StudentsPaymentsApi>

class StudentsPaymentsApiProviderImpl @Inject constructor(
    private val allApiProvider: AllApiProviderImpl
): StudentsPaymentsApiProvider {
    override fun provide() = allApiProvider.provideStudentsPaymentsApi()
}