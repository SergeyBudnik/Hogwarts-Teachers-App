package com.bdev.hengschoolteacher.services.students_payments

import com.bdev.hengschoolteacher.dao.StudentsPaymentsDao
import com.bdev.hengschoolteacher.dao.StudentsPaymentsModel
import com.bdev.hengschoolteacher.data.school.student_payment.ExistingStudentPayment
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

interface StudentsPaymentsStorageService {
    fun getAll(): List<ExistingStudentPayment>
    fun setAll(payments: List<ExistingStudentPayment>)
}

@EBean(scope = EBean.Scope.Singleton)
open class StudentsPaymentsStorageServiceImpl : StudentsPaymentsStorageService {
    @Bean
    lateinit var studentsPaymentsDao: StudentsPaymentsDao

    override fun getAll(): List<ExistingStudentPayment> {
        return studentsPaymentsDao.readValue().studentsPayments.values.toList()
    }

    override fun setAll(payments: List<ExistingStudentPayment>) {
        studentsPaymentsDao.writeValue(StudentsPaymentsModel(
                payments.map { it.id to it }.toMap()
        ))
    }
}
