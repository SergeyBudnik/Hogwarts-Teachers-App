package com.bdev.hengschoolteacher.services

import com.bdev.hengschoolteacher.services.lessons.LessonsService
import com.bdev.hengschoolteacher.services.students.StudentsStorageService
import com.bdev.hengschoolteacher.services.students.StudentsStorageServiceImpl
import com.bdev.hengschoolteacher.services.students_attendances.StudentsAttendancesProviderService
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

@EBean
open class LessonsAttendancesService {
    @Bean(StudentsStorageServiceImpl::class)
    lateinit var studentsStorageService: StudentsStorageService
    @Bean
    lateinit var lessonsService: LessonsService
    @Bean
    lateinit var studentsAttendancesProviderService: StudentsAttendancesProviderService

    fun isLessonAttendanceFilled(lessonId: Long, weekIndex: Int): Boolean {
        return lessonsService
                .getLessonStudents(lessonId = lessonId, weekIndex = weekIndex)
                .map { studentsAttendancesProviderService.getAttendance(lessonId, it.login, weekIndex) }
                .filter { it == null }
                .none()
    }
}
