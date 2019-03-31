package com.bdev.hengschoolteacher.async

import com.bdev.hengschoolteacher.async.common.SmartPromise
import com.bdev.hengschoolteacher.async.common.SmartTask.Companion.smartTask
import com.bdev.hengschoolteacher.data.school.lesson.LessonStatus
import com.bdev.hengschoolteacher.rest.LessonStatusRest
import com.bdev.hengschoolteacher.service.AuthService
import com.bdev.hengschoolteacher.service.LessonStatusService
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean
import org.androidannotations.rest.spring.annotations.RestService

@EBean
open class LessonStatusAsyncService : CommonAsyncService() {
    @RestService
    lateinit var lessonStatusRest: LessonStatusRest

    @Bean
    lateinit var lessonStatusService: LessonStatusService
    @Bean
    lateinit var authService: AuthService

    fun addLessonStatus(lessonStatus: LessonStatus): SmartPromise<Unit, Exception> {
        return smartTask {
            authenticateAll(
                    listOf(lessonStatusRest),
                    authService.getAuthInfo()
            )

            lessonStatus.id = trustSsl(lessonStatusRest).addLessonStatus(lessonStatus)

            lessonStatusService.addLessonStatus(lessonStatus)
        }
    }
}
