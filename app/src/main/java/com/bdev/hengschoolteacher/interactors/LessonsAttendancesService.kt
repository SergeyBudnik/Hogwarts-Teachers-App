package com.bdev.hengschoolteacher.interactors

import com.bdev.hengschoolteacher.interactors.lessons.LessonsInteractorImpl
import com.bdev.hengschoolteacher.interactors.students.StudentsStorageInteractor
import com.bdev.hengschoolteacher.interactors.students.StudentsStorageInteractorImpl
import com.bdev.hengschoolteacher.interactors.students_attendances.StudentsAttendancesProviderServiceImpl
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

interface LessonsAttendancesService {
    fun isLessonAttendanceFilled(lessonId: Long, weekIndex: Int): Boolean
}

@EBean
open class LessonsAttendancesServiceImpl : LessonsAttendancesService {
    @Bean(StudentsStorageInteractorImpl::class)
    lateinit var studentsStorageInteractor: StudentsStorageInteractor
    @Bean
    lateinit var lessonsService: LessonsInteractorImpl
    @Bean
    lateinit var studentsAttendancesProviderService: StudentsAttendancesProviderServiceImpl

    override fun isLessonAttendanceFilled(lessonId: Long, weekIndex: Int): Boolean {
        return lessonsService
                .getLessonStudents(lessonId = lessonId, weekIndex = weekIndex)
                .map { studentsAttendancesProviderService.getAttendance(lessonId, it.login, weekIndex) }
                .filter { it == null }
                .none()
    }
}
