package com.bdev.hengschoolteacher.service

import com.bdev.hengschoolteacher.data.school.group.Group
import com.bdev.hengschoolteacher.data.school.group.Lesson
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

@EBean
open class LessonsAttendancesService {
    @Bean
    lateinit var studentsService: StudentsService
    @Bean
    lateinit var studentsAttendancesService: StudentsAttendancesService

    fun isLessonAttendanceFilled(group: Group, lesson: Lesson): Boolean {
        return studentsService
                .getGroupStudents(group.id)
                .asSequence()
                .map { studentsAttendancesService.getAttendance(lesson.id, it.id) }
                .filter { it != null }
                .any()
    }
}
