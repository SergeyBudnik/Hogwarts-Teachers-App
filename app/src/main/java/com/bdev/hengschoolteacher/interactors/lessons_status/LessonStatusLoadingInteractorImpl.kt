package com.bdev.hengschoolteacher.interactors.lessons_status

import com.bdev.hengschoolteacher.async.CommonAsyncService
import com.bdev.hengschoolteacher.async.common.SmartPromise
import com.bdev.hengschoolteacher.async.common.SmartTask.Companion.smartTask
import com.bdev.hengschoolteacher.data.school.lesson.LessonStatus
import com.bdev.hengschoolteacher.rest.LessonStatusRest
import com.bdev.hengschoolteacher.interactors.auth.AuthStorageInteractorImpl
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean
import org.androidannotations.rest.spring.annotations.RestService

interface LessonStatusLoadingInteractor {
    fun load(): SmartPromise<Unit, Exception>
    fun addLessonStatus(lessonStatus: LessonStatus): SmartPromise<Unit, Exception>
}

@EBean
open class LessonStatusLoadingInteractorImpl : LessonStatusLoadingInteractor, CommonAsyncService() {
    @RestService
    lateinit var lessonStatusRest: LessonStatusRest

    @Bean
    lateinit var lessonStatusService: LessonStatusStorageInteractorImpl
    @Bean
    lateinit var authService: AuthStorageInteractorImpl

    override fun load(): SmartPromise<Unit, Exception> {
        return smartTask {
            authenticateAll(
                listOf(lessonStatusRest),
                authService.getAuthInfo()
            )

            val lessonsStatuses = lessonStatusRest.getAllLessonsStatuses(0, Long.MAX_VALUE) // ToDo

            lessonStatusService.setLessonsStatuses(lessonsStatuses)
        }
    }

    override fun addLessonStatus(lessonStatus: LessonStatus): SmartPromise<Unit, Exception> {
        return smartTask {
            authenticateAll(
                    listOf(lessonStatusRest),
                    authService.getAuthInfo()
            )

            lessonStatusService.addLessonStatus(
                    lessonStatus.withId(lessonStatusRest.addLessonStatus(lessonStatus) as Long)
            )
        }
    }
}
