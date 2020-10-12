package com.bdev.hengschoolteacher.service

import com.bdev.hengschoolteacher.data.school.group.GroupType
import com.bdev.hengschoolteacher.data.school.student.StudentAttendance
import com.bdev.hengschoolteacher.service.student_attendance.StudentsAttendancesProviderService
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

@EBean
open class StudentPriceService {
    @Bean
    lateinit var studentsAttendancesProviderService: StudentsAttendancesProviderService

    fun getTotalPrice(studentLogin: String): Long {
        return studentsAttendancesProviderService
                .getAllStudentAttendances(studentLogin)
                .filter { it.type.isPayed }
                .map { getAttendancePrice(it) }
                .fold(0L) { amount, value -> amount + value }
    }

    fun getMonthPrice(studentLogin: String, month: Int): Long {
        return studentsAttendancesProviderService
                .getMonthlyAttendances(studentLogin, month)
                .filter { it.type.isPayed }
                .map { getAttendancePrice(it) }
                .fold(0L) { amount, value -> amount + value }
    }

    fun getAttendancePrice(attendance: StudentAttendance): Int {
        val lengthInHalfOfHours = ((attendance.finishTime - attendance.startTime) / 1000 / 1800).toInt()

        return when (attendance.groupType) {
            GroupType.GROUP -> if (attendance.studentsInGroup == 1 && !attendance.ignoreSingleStudentPricing) {
                 when (lengthInHalfOfHours) {
                    1 -> 350
                    2 -> 700
                    3 -> 1050
                    4 -> 1400
                    else -> 700 /* ToDo: log */
                }
            } else {
                when (lengthInHalfOfHours) {
                    1 -> 240
                    2 -> 480
                    3 -> 700
                    4 -> 1000
                    else -> 700 /* ToDo: log */
                }
            }
            GroupType.INDIVIDUAL -> when (lengthInHalfOfHours) {
                1 -> 600
                2 -> 1200
                3 -> 1800
                4 -> 2400
                else -> 1200 /* ToDO: log */
            }
        }
    }
}