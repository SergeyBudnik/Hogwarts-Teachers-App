package com.bdev.hengschoolteacher.interactors.lessons_status

import com.bdev.hengschoolteacher.async.common.SmartPromise
import com.bdev.hengschoolteacher.async.common.SmartTask.Companion.smartTask
import com.bdev.hengschoolteacher.data.school.lesson.LessonStatus
import com.bdev.hengschoolteacher.interactors.auth.AuthStorageInteractorImpl
import com.bdev.hengschoolteacher.network.api.lessons_status.LessonsStatusApiProviderImpl
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

interface LessonStatusLoadingInteractor {
    fun load(): SmartPromise<Unit, Exception>
    fun addLessonStatus(lessonStatus: LessonStatus): SmartPromise<Unit, Exception>
}

@EBean
open class LessonStatusLoadingInteractorImpl : LessonStatusLoadingInteractor {
    @Bean
    lateinit var lessonsStatusApiProvider: LessonsStatusApiProviderImpl
    @Bean
    lateinit var lessonStatusService: LessonStatusStorageInteractorImpl
    @Bean
    lateinit var authService: AuthStorageInteractorImpl

    override fun load(): SmartPromise<Unit, Exception> {
        return smartTask {
            val lessonsStatuses = lessonsStatusApiProvider
                .provide()
                .getAllLessonsStatuses(0, Long.MAX_VALUE) // ToDo
                .execute()
                .body()!!

            lessonStatusService.setLessonsStatuses(lessonsStatuses)
        }
    }

    override fun addLessonStatus(lessonStatus: LessonStatus): SmartPromise<Unit, Exception> {
        return smartTask {
            lessonStatusService.addLessonStatus(
                    lessonStatus.withId(
                        lessonsStatusApiProvider.provide().addLessonStatus(lessonStatus).execute().body()!!
                    )
            )
        }
    }
}
