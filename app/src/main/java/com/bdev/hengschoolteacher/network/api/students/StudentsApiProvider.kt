package com.bdev.hengschoolteacher.network.api.students

import com.bdev.hengschoolteacher.network.api_provider.AllApiProvider
import com.bdev.hengschoolteacher.network.api_provider.ApiProvider
import javax.inject.Inject

interface StudentsApiProvider : ApiProvider<StudentsApi>

class StudentsApiProviderImpl @Inject constructor(
    private val allApiProvider: AllApiProvider
): StudentsApiProvider {
    override fun provide() = allApiProvider.provideStudentsApi()
}