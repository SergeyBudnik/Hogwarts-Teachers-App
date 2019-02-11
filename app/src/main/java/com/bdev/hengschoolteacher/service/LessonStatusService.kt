package com.bdev.hengschoolteacher.service

import com.bdev.hengschoolteacher.dao.LessonStatusDao
import com.bdev.hengschoolteacher.data.school.lesson.LessonStatus
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

@EBean
open class LessonStatusService {
    @Bean
    lateinit var lessonStatusDao: LessonStatusDao

    fun addLessonStatus(lessonStatus: LessonStatus) {
        lessonStatusDao.addLessonStatus(lessonStatus)
    }

    fun setLessonsStatuses(lessonsStatuses: List<LessonStatus>) {
        lessonStatusDao.setLessonsStatuses(lessonsStatuses)
    }

    fun getLessonStatus(lessonId: Long, lessonTime: Long): LessonStatus? {
        return lessonStatusDao
                .getLessonsStatuses()
                .filter { it.lessonId == lessonId }
                .filter { it.actionTime == lessonTime }
                .maxBy { it.id ?: -1 }
    }
}
