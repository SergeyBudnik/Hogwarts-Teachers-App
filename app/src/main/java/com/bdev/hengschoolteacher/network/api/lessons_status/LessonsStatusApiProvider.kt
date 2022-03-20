package com.bdev.hengschoolteacher.network.api.lessons_status

import com.bdev.hengschoolteacher.network.api_provider.AllApiProvider
import com.bdev.hengschoolteacher.network.api_provider.ApiProvider
import javax.inject.Inject

interface LessonsStatusApiProvider : ApiProvider<LessonsStatusApi>

class LessonsStatusApiProviderImpl @Inject constructor(
    private val allApiProvider: AllApiProvider
): LessonsStatusApiProvider {
    override fun provide() = allApiProvider.provideLessonsStatusApi()
}