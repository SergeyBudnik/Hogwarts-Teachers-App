package com.bdev.hengschoolteacher.service

import com.bdev.hengschoolteacher.dao.LessonsTransferDao
import com.bdev.hengschoolteacher.data.school.lesson.LessonTransfer
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

@EBean
open class LessonsTransferStorageService {
    @Bean
    lateinit var lessonsTransferDao: LessonsTransferDao

    fun setLessonsTransfers(lessonsTransfers: List<LessonTransfer>) {
        lessonsTransferDao.writeValue(lessonsTransfers)
    }

    fun addLessonTransfer(lessonTransfer: LessonTransfer) {
        lessonsTransferDao.writeValue(
                lessonsTransferDao.readValue().union(listOf(lessonTransfer)).toList()
        )
    }
}
