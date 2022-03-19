package com.bdev.hengschoolteacher.network.api.lessons_status

import com.bdev.hengschoolteacher.network.api_provider.AllApiProviderImpl
import com.bdev.hengschoolteacher.network.api_provider.ApiProvider
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

interface LessonsStatusApiProvider : ApiProvider<LessonsStatusApi>

@EBean
open class LessonsStatusApiProviderImpl : LessonsStatusApiProvider {
    @Bean
    lateinit var allApiProvider: AllApiProviderImpl

    override fun provide() = allApiProvider.provideLessonsStatusApi()
}