package com.bdev.hengschoolteacher.service

import com.bdev.hengschoolteacher.dao.StudentsPaymentsDao
import com.bdev.hengschoolteacher.dao.StudentsPaymentsModel
import com.bdev.hengschoolteacher.data.school.student_payment.ExistingStudentPayment
import com.bdev.hengschoolteacher.utils.TimeUtils
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

@EBean
open class StudentsPaymentsService {
    @Bean
    lateinit var studentsPaymentsDao: StudentsPaymentsDao

    fun getAllPayments(): List<ExistingStudentPayment> {
        return studentsPaymentsDao.readValue().studentsPayments.values.toList()
    }

    fun getPayment(id: Long): ExistingStudentPayment? {
        return studentsPaymentsDao.readValue().studentsPayments[id]
    }

    fun getPayments(studentLogin: String): List<ExistingStudentPayment> {
        return studentsPaymentsDao
                .readValue()
                .studentsPayments
                .values
                .asSequence()
                .filter { it.info.studentLogin == studentLogin }
                .toList()
    }

    fun getMonthPayments(studentLogin: String, month: Int): List<ExistingStudentPayment> {
        val startTime = TimeUtils().getMonthStart(month)
        val finishTime = TimeUtils().getMonthFinish(month)

        return studentsPaymentsDao
                .readValue()
                .studentsPayments
                .values
                .asSequence()
                .filter { it.info.studentLogin == studentLogin }
                .filter { it.info.time <= finishTime }
                .filter { it.info.time >= startTime }
                .toList()
    }

    fun getPaymentsToTeacher(teacherLogin: String, onlyUnprocessed: Boolean): List<ExistingStudentPayment> {
        return studentsPaymentsDao
                .readValue()
                .studentsPayments
                .values
                .filter { it.info.staffMemberLogin == teacherLogin }
                .filter { !onlyUnprocessed || !it.processed }
                .toList()
    }

    fun setPayments(paymentExistings: List<ExistingStudentPayment>) {
        studentsPaymentsDao.writeValue(StudentsPaymentsModel(
                paymentExistings.map { it.id to it }.toMap()
        ))
    }

    fun addPayment(paymentExisting: ExistingStudentPayment) {
        studentsPaymentsDao.writeValue(StudentsPaymentsModel(
                studentsPaymentsDao
                        .readValue()
                        .studentsPayments
                        .plus(Pair(paymentExisting.id, paymentExisting))
        ))
    }
}
