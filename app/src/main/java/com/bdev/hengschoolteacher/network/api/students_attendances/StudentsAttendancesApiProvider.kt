package com.bdev.hengschoolteacher.network.api.students_attendances

import com.bdev.hengschoolteacher.network.api_provider.AllApiProvider
import com.bdev.hengschoolteacher.network.api_provider.ApiProvider
import javax.inject.Inject

interface StudentsAttendancesApiProvider : ApiProvider<StudentsAttendancesApi>

class StudentsAttendancesApiProviderImpl @Inject constructor(
    private val allApiProvider: AllApiProvider
) : StudentsAttendancesApiProvider {
    override fun provide() = allApiProvider.provideStudentsAttendancesApi()
}