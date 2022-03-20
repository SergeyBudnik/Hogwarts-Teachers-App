package com.bdev.hengschoolteacher.interactors.students_payments

import com.bdev.hengschoolteacher.data.school.student_payment.ExistingStudentPayment
import javax.inject.Inject

interface StudentsPaymentsModifierInteractor {
    fun setAll(payments: List<ExistingStudentPayment>)
    fun add(payment: ExistingStudentPayment)
}

class StudentsPaymentsModifierInteractorImpl @Inject constructor(
    private val studentsPaymentsStorageInteractor: StudentsPaymentsStorageInteractor
): StudentsPaymentsModifierInteractor {
    override fun setAll(payments: List<ExistingStudentPayment>) {
        studentsPaymentsStorageInteractor.setAll(payments = payments)
    }

    override fun add(payment: ExistingStudentPayment) {
        studentsPaymentsStorageInteractor.setAll(
                payments = studentsPaymentsStorageInteractor.getAll().plus(payment)
        )
    }
}