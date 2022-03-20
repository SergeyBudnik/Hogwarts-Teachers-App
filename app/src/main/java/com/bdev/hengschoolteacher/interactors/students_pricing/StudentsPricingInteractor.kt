package com.bdev.hengschoolteacher.interactors.students_pricing

import com.bdev.hengschoolteacher.data.school.student.StudentAttendance
import com.bdev.hengschoolteacher.interactors.groups.GroupsStudentsProviderInteractor
import com.bdev.hengschoolteacher.interactors.lessons.LessonsInteractor
import com.bdev.hengschoolteacher.interactors.lessons_pricing.LessonsPricingInteractor
import com.bdev.hengschoolteacher.interactors.students_attendances.StudentsAttendancesProviderInteractor
import com.bdev.hengschoolteacher.utils.TimeUtils
import javax.inject.Inject

interface StudentsPricingInteractor {
    fun getTotalExpectedPrice(studentLogin: String): Long
    fun getTotalPrice(studentLogin: String): Long
    fun getMonthExpectedPrice(studentLogin: String, month: Int): Long
    fun getMonthPrice(studentLogin: String, month: Int): Long
    fun getAttendancePrice(attendance: StudentAttendance): Int
}

class StudentsPricingInteractorImpl @Inject constructor(
    private val studentsAttendancesProviderInteractor: StudentsAttendancesProviderInteractor,
    private val lessonsService: LessonsInteractor,
    private val lessonsPricingInteractor: LessonsPricingInteractor,
    private val groupStudentsProviderInteractor: GroupsStudentsProviderInteractor
): StudentsPricingInteractor {
    override fun getTotalExpectedPrice(studentLogin: String): Long {
        val monthStart = TimeUtils().getMonthStart(
                month = TimeUtils().getCurrentMonth()
        )

        val previousMonthsPrice = studentsAttendancesProviderInteractor
                .getAllStudentAttendances(studentLogin)
                .filter { it.type.isPayed }
                .filter { it.startTime < monthStart }
                .map { getAttendancePrice(it) }
                .fold(0L) { amount, value -> amount + value }

        val currentMonthPrice = getMonthExpectedPrice(
                studentLogin = studentLogin,
                month = TimeUtils().getCurrentMonth()
        )

        return previousMonthsPrice + currentMonthPrice
    }

    override fun getTotalPrice(studentLogin: String): Long {
        return studentsAttendancesProviderInteractor
                .getAllStudentAttendances(studentLogin)
                .filter { it.type.isPayed }
                .map { getAttendancePrice(it) }
                .fold(0L) { amount, value -> amount + value }
    }

    override fun getMonthExpectedPrice(studentLogin: String, month: Int): Long {
        val lessonsInMonth = lessonsService.getAllStudentLessonsInMonth(
                studentLogin = studentLogin,
                month = month
        )

        val monthlyUnpayableAttendances = studentsAttendancesProviderInteractor.getMonthlyAttendances(
                studentLogin = studentLogin,
                month = month
        ).filter { !it.type.isPayed }

        return lessonsInMonth.map { lessonInfo ->
            val isPayable = !monthlyUnpayableAttendances.any { it.startTime == lessonInfo.second }

            if (isPayable) {
                lessonsPricingInteractor.getPrice(
                        groupType = lessonInfo.first.group.type,
                        lengthIn30m = lessonInfo.first.lesson.durationIn30m(),
                        amountOfStudents = groupStudentsProviderInteractor.getAll(groupId = lessonInfo.first.group.id, time = lessonInfo.second).size,
                        ignoreSingleStudentPrice = lessonInfo.first.lesson.ignoreSingleStudentPricing
                )
            } else {
                0
            }
        }.fold(0) { amount, value -> amount + value }.toLong()
    }

    override fun getMonthPrice(studentLogin: String, month: Int): Long {
        return studentsAttendancesProviderInteractor
                .getMonthlyAttendances(studentLogin, month)
                .filter { it.type.isPayed }
                .map { getAttendancePrice(it) }
                .fold(0L) { amount, value -> amount + value }
    }

    override fun getAttendancePrice(attendance: StudentAttendance): Int {
        return lessonsPricingInteractor.getPrice(
                groupType = attendance.groupType,
                lengthIn30m = ((attendance.finishTime - attendance.startTime) / 1000 / 1800).toInt(),
                amountOfStudents = attendance.studentsInGroup,
                ignoreSingleStudentPrice = attendance.ignoreSingleStudentPricing
        )
    }
}