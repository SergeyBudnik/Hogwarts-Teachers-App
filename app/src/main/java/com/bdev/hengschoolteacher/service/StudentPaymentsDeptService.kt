package com.bdev.hengschoolteacher.service

import com.bdev.hengschoolteacher.data.school.group.GroupType
import com.bdev.hengschoolteacher.data.school.student.StudentAttendance
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

@EBean
open class StudentPaymentsDeptService {
    @Bean
    lateinit var studentsAttendancesService: StudentsAttendancesService
    @Bean
    lateinit var studentPaymentsService: StudentsPaymentsService

    fun getStudentDept(studentId: Long): Int {
        val allAttendances = studentsAttendancesService.getAllStudentAttendances(studentId)
        val allPayableAttendances = allAttendances.filter { it.type != StudentAttendance.Type.VALID_SKIP }

        val payed = studentPaymentsService
                .getPayments(studentId)
                .fold(0L) { amount, value -> amount + value.amount }
                .toInt()

        var price = 0

        for (attendance in allPayableAttendances) {
            val lengthInHalfOfHours = ((attendance.finishTime - attendance.startTime) / 1000 / 1800).toInt()

            if (attendance.groupType == GroupType.GROUP) {
                if (attendance.studentsInGroup == 1) {
                    price += when (lengthInHalfOfHours) {
                        1 -> 350
                        2 -> 700
                        3 -> 1050
                        4 -> 1400
                        else -> 700 /* ToDo: log */
                    }
                } else {
                    price += when (lengthInHalfOfHours) {
                        1 -> 240
                        2 -> 480
                        3 -> 700
                        4 -> 1000
                        else -> 700 /* ToDo: log */
                    }
                }
            } else if (attendance.groupType == GroupType.INDIVIDUAL) {
                price += when (lengthInHalfOfHours) {
                    1 -> 550
                    2 -> 1100
                    3 -> 1650
                    4 -> 2200
                    else -> 1100 /* ToDO: log */
                }
            }
        }

        return price - payed
    }
}
