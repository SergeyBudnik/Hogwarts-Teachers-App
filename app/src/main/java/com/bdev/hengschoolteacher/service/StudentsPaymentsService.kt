package com.bdev.hengschoolteacher.service

import com.bdev.hengschoolteacher.dao.StudentsPaymentsDao
import com.bdev.hengschoolteacher.dao.StudentsPaymentsModel
import com.bdev.hengschoolteacher.data.school.student_payment.StudentPayment
import com.bdev.hengschoolteacher.utils.TimeUtils
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

@EBean
open class StudentsPaymentsService {
    @Bean
    lateinit var studentsPaymentsDao: StudentsPaymentsDao

    fun getAllPayments(): List<StudentPayment> {
        return studentsPaymentsDao.readValue().studentsPayments.values.toList()
    }

    fun getPayment(id: Long): StudentPayment? {
        return studentsPaymentsDao.readValue().studentsPayments[id]
    }

    fun getPayments(studentId: Long): List<StudentPayment> {
        return studentsPaymentsDao
                .readValue()
                .studentsPayments
                .values
                .asSequence()
                .filter { it.studentId == studentId }
                .toList()
    }

    fun getMonthPayments(studentId: Long, month: Int): Long {
        val startTime = TimeUtils().getMonthStart(month)
        val finishTime = TimeUtils().getMonthFinish(month)

        return studentsPaymentsDao
                .readValue()
                .studentsPayments
                .values
                .asSequence()
                .filter { it.studentId == studentId }
                .filter { it.time <= finishTime }
                .filter { it.time >= startTime }
                .map { it.amount }
                .fold(0L) { p1, p2 -> p1 + p2 }
    }

    fun getPaymentsToTeacher(teacherId: Long, onlyUnprocessed: Boolean): List<StudentPayment> {
        return studentsPaymentsDao
                .readValue()
                .studentsPayments
                .values
                .filter { it.teacherId == teacherId }
                .filter { !onlyUnprocessed || !it.processed }
                .toList()
    }

    fun setPayments(payments: List<StudentPayment>) {
        studentsPaymentsDao.writeValue(StudentsPaymentsModel(
                payments.map { it.id to it }.toMap()
        ))
    }

    fun addPayment(payment: StudentPayment) {
        studentsPaymentsDao.writeValue(StudentsPaymentsModel(
                studentsPaymentsDao
                        .readValue()
                        .studentsPayments
                        .plus(Pair(payment.id, payment))
        ))
    }
}
