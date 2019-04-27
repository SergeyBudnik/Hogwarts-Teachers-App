package com.bdev.hengschoolteacher.service

import com.bdev.hengschoolteacher.dao.LessonStatusDao
import com.bdev.hengschoolteacher.dao.LessonStatusModel
import com.bdev.hengschoolteacher.data.school.lesson.LessonStatus
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

@EBean
open class LessonStatusService {
    @Bean
    lateinit var lessonStatusDao: LessonStatusDao

    fun addLessonStatus(lessonStatus: LessonStatus) {
        lessonStatusDao.writeValue(LessonStatusModel(
                lessonStatusDao.readValue().lessonsStatuses.union(listOf(lessonStatus)).toList()
        ))
    }

    fun setLessonsStatuses(lessonsStatuses: List<LessonStatus>) {
        lessonStatusDao.writeValue(LessonStatusModel(
                lessonsStatuses
        ))
    }

    fun getLessonStatus(lessonId: Long, lessonTime: Long): LessonStatus? {
        return lessonStatusDao
                .readValue()
                .lessonsStatuses
                .filter { it.lessonId == lessonId }
                .filter { it.actionTime == lessonTime }
                .maxBy { it.id ?: -1 }
    }
}
