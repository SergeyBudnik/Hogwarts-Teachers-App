package com.bdev.hengschoolteacher.interactors

import com.bdev.hengschoolteacher.interactors.lessons.LessonsInteractor
import com.bdev.hengschoolteacher.interactors.students_attendances.StudentsAttendancesProviderInteractor
import javax.inject.Inject

interface LessonsAttendancesService {
    fun isLessonAttendanceFilled(lessonId: Long, weekIndex: Int): Boolean
}

class LessonsAttendancesServiceImpl @Inject constructor(
    private val lessonsService: LessonsInteractor,
    private val studentsAttendancesProviderInteractor: StudentsAttendancesProviderInteractor
): LessonsAttendancesService {
    override fun isLessonAttendanceFilled(lessonId: Long, weekIndex: Int): Boolean {
        return lessonsService
                .getLessonStudents(lessonId = lessonId, weekIndex = weekIndex)
                .map { studentsAttendancesProviderInteractor.getAttendance(lessonId, it.login, weekIndex) }
                .filter { it == null }
                .none()
    }
}
