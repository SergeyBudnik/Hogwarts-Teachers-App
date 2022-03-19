package com.bdev.hengschoolteacher.interactors.students_pricing

import com.bdev.hengschoolteacher.data.school.student.StudentAttendance
import com.bdev.hengschoolteacher.interactors.groups.GroupStudentsProviderService
import com.bdev.hengschoolteacher.interactors.groups.GroupStudentsProviderServiceImpl
import com.bdev.hengschoolteacher.interactors.groups.GroupsStorageInteractor
import com.bdev.hengschoolteacher.interactors.groups.GroupsStorageInteractorImpl
import com.bdev.hengschoolteacher.interactors.lessons.LessonsInteractorImpl
import com.bdev.hengschoolteacher.interactors.lessons_pricing.LessonsPricingService
import com.bdev.hengschoolteacher.interactors.lessons_pricing.LessonsPricingServiceImpl
import com.bdev.hengschoolteacher.interactors.students.StudentsStorageInteractor
import com.bdev.hengschoolteacher.interactors.students.StudentsStorageInteractorImpl
import com.bdev.hengschoolteacher.interactors.students_attendances.StudentsAttendancesProviderServiceImpl
import com.bdev.hengschoolteacher.utils.TimeUtils
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

interface StudentsPricingService {
    fun getTotalExpectedPrice(studentLogin: String): Long
    fun getTotalPrice(studentLogin: String): Long
    fun getMonthExpectedPrice(studentLogin: String, month: Int): Long
    fun getMonthPrice(studentLogin: String, month: Int): Long
    fun getAttendancePrice(attendance: StudentAttendance): Int
}

@EBean(scope = EBean.Scope.Singleton)
open class StudentsPricingServiceImpl : StudentsPricingService {
    @Bean
    lateinit var studentsAttendancesProviderService: StudentsAttendancesProviderServiceImpl

    @Bean
    lateinit var lessonsService: LessonsInteractorImpl

    @Bean(StudentsStorageInteractorImpl::class)
    lateinit var studentsStorageInteractor: StudentsStorageInteractor

    @Bean(LessonsPricingServiceImpl::class)
    lateinit var lessonsPricingService: LessonsPricingService

    @Bean(GroupsStorageInteractorImpl::class)
    lateinit var groupsStorageInteractor: GroupsStorageInteractor
    @Bean(GroupStudentsProviderServiceImpl::class)
    lateinit var groupStudentsProviderService: GroupStudentsProviderService

    override fun getTotalExpectedPrice(studentLogin: String): Long {
        val monthStart = TimeUtils().getMonthStart(
                month = TimeUtils().getCurrentMonth()
        )

        val previousMonthsPrice = studentsAttendancesProviderService
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
        return studentsAttendancesProviderService
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

        val monthlyUnpayableAttendances = studentsAttendancesProviderService.getMonthlyAttendances(
                studentLogin = studentLogin,
                month = month
        ).filter { !it.type.isPayed }

        return lessonsInMonth.map { lessonInfo ->
            val isPayable = !monthlyUnpayableAttendances.any { it.startTime == lessonInfo.second }

            if (isPayable) {
                lessonsPricingService.getPrice(
                        groupType = lessonInfo.first.group.type,
                        lengthIn30m = lessonInfo.first.lesson.durationIn30m(),
                        amountOfStudents = groupStudentsProviderService.getAll(groupId = lessonInfo.first.group.id, time = lessonInfo.second).size,
                        ignoreSingleStudentPrice = lessonInfo.first.lesson.ignoreSingleStudentPricing
                )
            } else {
                0
            }
        }.fold(0) { amount, value -> amount + value }.toLong()
    }

    override fun getMonthPrice(studentLogin: String, month: Int): Long {
        return studentsAttendancesProviderService
                .getMonthlyAttendances(studentLogin, month)
                .filter { it.type.isPayed }
                .map { getAttendancePrice(it) }
                .fold(0L) { amount, value -> amount + value }
    }

    override fun getAttendancePrice(attendance: StudentAttendance): Int {
        return lessonsPricingService.getPrice(
                groupType = attendance.groupType,
                lengthIn30m = ((attendance.finishTime - attendance.startTime) / 1000 / 1800).toInt(),
                amountOfStudents = attendance.studentsInGroup,
                ignoreSingleStudentPrice = attendance.ignoreSingleStudentPricing
        )
    }
}