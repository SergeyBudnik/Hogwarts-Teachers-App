package com.bdev.hengschoolteacher.service

import com.bdev.hengschoolteacher.dao.StudentsPaymentsDao
import com.bdev.hengschoolteacher.data.school.student.Student
import com.bdev.hengschoolteacher.data.school.student.StudentPayment
import com.bdev.hengschoolteacher.utils.TimeUtils
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

@EBean
open class StudentsPaymentsService {
    @Bean
    lateinit var studentsPaymentsDao: StudentsPaymentsDao

    fun getAllPayments(): List<StudentPayment> {
        return studentsPaymentsDao.getPayments()
    }

    fun getPayments(studentId: Long): List<StudentPayment> {
        return studentsPaymentsDao
                .getPayments()
                .asSequence()
                .filter { it.studentId == studentId }
                .toList()
    }

    fun getMonthPayments(student: Student, month: Int): Long {
        val startTime = TimeUtils().getMonthStart(month)
        val finishTime = TimeUtils().getMonthFinish(month)

        return studentsPaymentsDao
                .getPayments()
                .asSequence()
                .filter { it.studentId == student.id }
                .filter { it.time <= finishTime }
                .filter { it.time >= startTime }
                .map { it.amount }
                .fold(0L) { p1, p2 -> p1 + p2 }
    }

    fun setPayments(payments: List<StudentPayment>) {
        studentsPaymentsDao.setPayments(payments)
    }

    fun addPayment(payment: StudentPayment) {
        studentsPaymentsDao.addPayment(payment)
    }
}
