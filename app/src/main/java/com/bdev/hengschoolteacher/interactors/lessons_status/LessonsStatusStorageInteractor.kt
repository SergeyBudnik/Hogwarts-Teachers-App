package com.bdev.hengschoolteacher.interactors.lessons_status

import com.bdev.hengschoolteacher.dao.LessonStatusDao
import com.bdev.hengschoolteacher.dao.LessonStatusModel
import com.bdev.hengschoolteacher.data.school.lesson.LessonStatus
import javax.inject.Inject

interface LessonsStatusStorageInteractor {
    fun addLessonStatus(lessonStatus: LessonStatus)
    fun setLessonsStatuses(lessonsStatuses: List<LessonStatus>)
    fun getLessonStatus(lessonId: Long, lessonTime: Long): LessonStatus?
}

class LessonsStatusStorageInteractorImpl @Inject constructor(
    private val lessonStatusDao: LessonStatusDao
): LessonsStatusStorageInteractor {
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
            .maxByOrNull { it.id ?: -1 }
    }
}

