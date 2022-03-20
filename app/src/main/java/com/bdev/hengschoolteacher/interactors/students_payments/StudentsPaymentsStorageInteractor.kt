package com.bdev.hengschoolteacher.interactors.students_payments

import com.bdev.hengschoolteacher.dao.StudentsPaymentsDao
import com.bdev.hengschoolteacher.dao.StudentsPaymentsModel
import com.bdev.hengschoolteacher.data.school.student_payment.ExistingStudentPayment
import javax.inject.Inject

interface StudentsPaymentsStorageInteractor {
    fun getAll(): List<ExistingStudentPayment>
    fun setAll(payments: List<ExistingStudentPayment>)
}

class StudentsPaymentsStorageInteractorImpl @Inject constructor(
    private val studentsPaymentsDao: StudentsPaymentsDao
): StudentsPaymentsStorageInteractor {
    override fun getAll(): List<ExistingStudentPayment> {
        return studentsPaymentsDao.readValue().studentsPayments.values.toList()
    }

    override fun setAll(payments: List<ExistingStudentPayment>) {
        studentsPaymentsDao.writeValue(StudentsPaymentsModel(
                payments.map { it.id to it }.toMap()
        ))
    }
}
