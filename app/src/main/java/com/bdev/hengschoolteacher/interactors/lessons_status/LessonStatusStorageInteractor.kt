package com.bdev.hengschoolteacher.interactors.lessons_status

import com.bdev.hengschoolteacher.dao.LessonStatusDaoImpl
import com.bdev.hengschoolteacher.dao.LessonStatusModel
import com.bdev.hengschoolteacher.data.school.lesson.LessonStatus
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

interface LessonStatusStorageInteractor {
    fun addLessonStatus(lessonStatus: LessonStatus)
    fun setLessonsStatuses(lessonsStatuses: List<LessonStatus>)
    fun getLessonStatus(lessonId: Long, lessonTime: Long): LessonStatus?
}

@EBean
open class LessonStatusStorageInteractorImpl : LessonStatusStorageInteractor {
    @Bean
    lateinit var lessonStatusDao: LessonStatusDaoImpl

    override fun addLessonStatus(lessonStatus: LessonStatus) {
        lessonStatusDao.writeValue(
            LessonStatusModel(
                lessonStatusDao.readValue().lessonsStatuses.union(listOf(lessonStatus)).toList()
            )
        )
    }

    override fun setLessonsStatuses(lessonsStatuses: List<LessonStatus>) {
        lessonStatusDao.writeValue(
            LessonStatusModel(
                lessonsStatuses
            )
        )
    }

    override fun getLessonStatus(lessonId: Long, lessonTime: Long): LessonStatus? {
        return lessonStatusDao
            .readValue()
            .lessonsStatuses
            .filter { it.lessonId == lessonId }
            .filter { it.actionTime == lessonTime }
            .maxBy { it.id ?: -1 }
    }
}

