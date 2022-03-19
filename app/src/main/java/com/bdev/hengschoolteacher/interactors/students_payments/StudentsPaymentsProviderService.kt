package com.bdev.hengschoolteacher.interactors.students_payments

import com.bdev.hengschoolteacher.data.school.student_payment.ExistingStudentPayment
import com.bdev.hengschoolteacher.utils.TimeUtils
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

interface StudentsPaymentsProviderService {
    fun getAll(): List<ExistingStudentPayment>
    fun getByPaymentId(paymentId: Long): ExistingStudentPayment?
    fun getForStudent(studentLogin: String): List<ExistingStudentPayment>
    fun getForStudentForMonth(studentLogin: String, monthIndex: Int): List<ExistingStudentPayment>
    fun getForTeacher(teacherLogin: String, onlyUnprocessed: Boolean): List<ExistingStudentPayment>
}

@EBean(scope = EBean.Scope.Singleton)
open class StudentsPaymentsProviderServiceImpl : StudentsPaymentsProviderService {
    @Bean(StudentsPaymentsStorageServiceImpl::class)
    lateinit var studentsPaymentsStorageService: StudentsPaymentsStorageService

    override fun getAll(): List<ExistingStudentPayment> {
        return studentsPaymentsStorageService.getAll()
    }

    override fun getByPaymentId(paymentId: Long): ExistingStudentPayment? {
        return studentsPaymentsStorageService.getAll().find { it.id == paymentId }
    }

    override fun getForStudent(studentLogin: String): List<ExistingStudentPayment> {
        return studentsPaymentsStorageService.getAll().filter { it.info.studentLogin == studentLogin }
    }

    override fun getForStudentForMonth(studentLogin: String, monthIndex: Int): List<ExistingStudentPayment> {
        val startTime = TimeUtils().getMonthStart(monthIndex)
        val finishTime = TimeUtils().getMonthFinish(monthIndex)

        return studentsPaymentsStorageService
                .getAll()
                .filter { it.info.studentLogin == studentLogin }
                .filter { it.info.time <= finishTime }
                .filter { it.info.time >= startTime }
    }

    override fun getForTeacher(teacherLogin: String, onlyUnprocessed: Boolean): List<ExistingStudentPayment> {
        return studentsPaymentsStorageService
                .getAll()
                .filter { it.info.staffMemberLogin == teacherLogin }
                .filter { !onlyUnprocessed || !it.processed }
    }
}