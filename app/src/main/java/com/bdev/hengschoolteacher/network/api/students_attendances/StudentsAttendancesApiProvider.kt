package com.bdev.hengschoolteacher.network.api.students_attendances

import com.bdev.hengschoolteacher.network.api_provider.AllApiProviderImpl
import com.bdev.hengschoolteacher.network.api_provider.ApiProvider
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

interface StudentsAttendancesApiProvider : ApiProvider<StudentsAttendancesApi>

@EBean
open class StudentsAttendancesApiProviderImpl : StudentsAttendancesApiProvider {
    @Bean
    lateinit var allApiProvider: AllApiProviderImpl

    override fun provide() = allApiProvider.provideStudentsAttendancesApi()
}