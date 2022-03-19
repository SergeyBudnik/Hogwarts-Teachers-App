package com.bdev.hengschoolteacher.network.api.students

import com.bdev.hengschoolteacher.network.api_provider.AllApiProviderImpl
import com.bdev.hengschoolteacher.network.api_provider.ApiProvider
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

interface StudentsApiProvider : ApiProvider<StudentsApi>

@EBean
open class StudentsApiProviderImpl : StudentsApiProvider {
    @Bean
    lateinit var allApiProvider: AllApiProviderImpl

    override fun provide() = allApiProvider.provideStudentsApi()
}