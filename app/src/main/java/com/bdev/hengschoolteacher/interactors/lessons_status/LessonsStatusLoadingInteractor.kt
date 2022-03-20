package com.bdev.hengschoolteacher.interactors.lessons_status

import com.bdev.hengschoolteacher.async.common.SmartPromise
import com.bdev.hengschoolteacher.async.common.SmartTask.Companion.smartTask
import com.bdev.hengschoolteacher.data.school.lesson.LessonStatus
import com.bdev.hengschoolteacher.network.api.lessons_status.LessonsStatusApiProvider
import javax.inject.Inject

interface LessonsStatusLoadingInteractor {
    fun load(): SmartPromise<Unit, Exception>
    fun addLessonStatus(lessonStatus: LessonStatus): SmartPromise<Unit, Exception>
}

class LessonsStatusLoadingInteractorImpl @Inject constructor(
    private val lessonsStatusApiProvider: LessonsStatusApiProvider,
    private val lessonsStatusService: LessonsStatusStorageInteractor
): LessonsStatusLoadingInteractor {
    override fun load(): SmartPromise<Unit, Exception> {
        return smartTask {
            val lessonsStatuses = lessonsStatusApiProvider
                .provide()
                .getAllLessonsStatuses(0, Long.MAX_VALUE) // ToDo
                .execute()
                .body()!!

            lessonsStatusService.setLessonsStatuses(lessonsStatuses)
        }
    }

    override fun addLessonStatus(lessonStatus: LessonStatus): SmartPromise<Unit, Exception> {
        return smartTask {
            lessonsStatusService.addLessonStatus(
                    lessonStatus.withId(
                        lessonsStatusApiProvider.provide().addLessonStatus(lessonStatus).execute().body()!!
                    )
            )
        }
    }
}
