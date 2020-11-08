package com.bdev.hengschoolteacher.services.students_pricing

import com.bdev.hengschoolteacher.data.school.student.StudentAttendance
import com.bdev.hengschoolteacher.services.lessons.LessonsService
import com.bdev.hengschoolteacher.services.lessons_pricing.LessonsPricingService
import com.bdev.hengschoolteacher.services.lessons_pricing.LessonsPricingServiceImpl
import com.bdev.hengschoolteacher.services.students_attendances.StudentsAttendancesProviderService
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

interface StudentsPricingService {
    fun getTotalPrice(studentLogin: String): Long
    fun getMonthPrice(studentLogin: String, month: Int): Long
    fun getAttendancePrice(attendance: StudentAttendance): Int
}

@EBean(scope = EBean.Scope.Singleton)
open class StudentsPricingServiceImpl : StudentsPricingService {
    @Bean
    lateinit var studentsAttendancesProviderService: StudentsAttendancesProviderService

    @Bean
    lateinit var lessonsService: LessonsService

    @Bean(LessonsPricingServiceImpl::class)
    lateinit var lessonsPricingService: LessonsPricingService

    override fun getTotalPrice(studentLogin: String): Long {
        return studentsAttendancesProviderService
                .getAllStudentAttendances(studentLogin)
                .filter { it.type.isPayed }
                .map { getAttendancePrice(it) }
                .fold(0L) { amount, value -> amount + value }
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