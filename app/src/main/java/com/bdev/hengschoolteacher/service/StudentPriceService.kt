package com.bdev.hengschoolteacher.service

import com.bdev.hengschoolteacher.data.school.group.GroupType
import com.bdev.hengschoolteacher.data.school.student.StudentAttendance
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

@EBean
open class StudentPriceService {
    @Bean
    lateinit var studentsAttendancesService: StudentsAttendancesService

    fun getTotalPrice(studentId: Long): Long {
        return studentsAttendancesService
                .getAllStudentAttendances(studentId)
                .filter { it.type.isPayed }
                .map { getAttendancePrice(it) }
                .fold(0L) { amount, value -> amount + value }
    }

    fun getMonthPrice(studentId: Long, month: Int): Long {
        return studentsAttendancesService
                .getMonthlyAttendances(studentId, month)
                .filter { it.type.isPayed }
                .map { getAttendancePrice(it) }
                .fold(0L) { amount, value -> amount + value }
    }

    fun getAttendancePrice(attendance: StudentAttendance): Int {
        val lengthInHalfOfHours = ((attendance.finishTime - attendance.startTime) / 1000 / 1800).toInt()

        return when (attendance.groupType) {
            GroupType.GROUP -> if (attendance.studentsInGroup == 1) {
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
                1 -> 550
                2 -> 1100
                3 -> 1650
                4 -> 2200
                else -> 1100 /* ToDO: log */
            }
        }
    }
}