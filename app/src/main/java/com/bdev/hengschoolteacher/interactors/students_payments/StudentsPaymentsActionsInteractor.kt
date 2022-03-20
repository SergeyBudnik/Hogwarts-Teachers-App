package com.bdev.hengschoolteacher.interactors.students_payments

import com.bdev.hengschoolteacher.async.common.SmartPromise
import com.bdev.hengschoolteacher.async.common.SmartTask.Companion.smartTask
import com.bdev.hengschoolteacher.data.school.student_payment.ExistingStudentPayment
import com.bdev.hengschoolteacher.data.school.student_payment.NewStudentPayment
import com.bdev.hengschoolteacher.network.api.students_payments.StudentsPaymentsApiProvider
import javax.inject.Inject

interface StudentsPaymentsActionsInteractor {
    fun addPayment(newStudentPayment: NewStudentPayment): SmartPromise<Unit, Exception>
    fun setPaymentProcessed(paymentId: Long): SmartPromise<ExistingStudentPayment, Exception>
}

class StudentsPaymentsActionsInteractorImpl @Inject constructor(
    private val studentsPaymentsApiProvider: StudentsPaymentsApiProvider,
    private val studentsPaymentsModifierInteractor: StudentsPaymentsModifierInteractor,
    private val studentsPaymentsProviderInteractor: StudentsPaymentsProviderInteractor
): StudentsPaymentsActionsInteractor {

    override fun addPayment(newStudentPayment: NewStudentPayment): SmartPromise<Unit, Exception> {
        return smartTask {
            val studentPaymentId = studentsPaymentsApiProvider
                .provide()
                .addStudentPayment(newStudentPayment)
                .execute()
                .body()!!

            studentsPaymentsModifierInteractor.add(
                    payment = ExistingStudentPayment(
                            id = studentPaymentId,
                            info = newStudentPayment.info,
                            processed = false
                    )
            )
        }
    }

    override fun setPaymentProcessed(paymentId: Long): SmartPromise<ExistingStudentPayment, Exception> {
        return smartTask {
            val oldStudentPayment = studentsPaymentsProviderInteractor.getByPaymentId(
                    paymentId = paymentId
            ) ?: throw RuntimeException()

            studentsPaymentsApiProvider
                .provide()
                .setStudentPaymentsProcessed(
                    paymentId = paymentId,
                    processed = !oldStudentPayment.processed
                )
                .execute()

            val newStudentPayment = oldStudentPayment.copy(
                    processed = !oldStudentPayment.processed
            )

            studentsPaymentsModifierInteractor.add(payment = newStudentPayment)

            return@smartTask newStudentPayment
        }
    }
}
