package com.bdev.hengschoolteacher.services.students_pricing

import com.bdev.hengschoolteacher.data.school.student.StudentAttendance
import com.bdev.hengschoolteacher.services.groups.GroupStudentsProviderService
import com.bdev.hengschoolteacher.services.groups.GroupStudentsProviderServiceImpl
import com.bdev.hengschoolteacher.services.groups.GroupsStorageService
import com.bdev.hengschoolteacher.services.groups.GroupsStorageServiceImpl
import com.bdev.hengschoolteacher.services.lessons.LessonsService
import com.bdev.hengschoolteacher.services.lessons_pricing.LessonsPricingService
import com.bdev.hengschoolteacher.services.lessons_pricing.LessonsPricingServiceImpl
import com.bdev.hengschoolteacher.services.students.StudentsStorageService
import com.bdev.hengschoolteacher.services.students.StudentsStorageServiceImpl
import com.bdev.hengschoolteacher.services.students_attendances.StudentsAttendancesProviderService
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
    lateinit var studentsAttendancesProviderService: StudentsAttendancesProviderService

    @Bean
    lateinit var lessonsService: LessonsService

    @Bean(StudentsStorageServiceImpl::class)
    lateinit var studentsStorageService: StudentsStorageService

    @Bean(LessonsPricingServiceImpl::class)
    lateinit var lessonsPricingService: LessonsPricingService

    @Bean(GroupsStorageServiceImpl::class)
    lateinit var groupsStorageService: GroupsStorageService
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