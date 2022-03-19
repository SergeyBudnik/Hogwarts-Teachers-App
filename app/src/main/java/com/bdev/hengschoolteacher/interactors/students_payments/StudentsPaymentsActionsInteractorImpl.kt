package com.bdev.hengschoolteacher.interactors.students_payments

import com.bdev.hengschoolteacher.async.common.SmartPromise
import com.bdev.hengschoolteacher.async.common.SmartTask.Companion.smartTask
import com.bdev.hengschoolteacher.data.school.student_payment.ExistingStudentPayment
import com.bdev.hengschoolteacher.data.school.student_payment.NewStudentPayment
import com.bdev.hengschoolteacher.interactors.auth.AuthStorageInteractorImpl
import com.bdev.hengschoolteacher.network.api.students_payments.StudentsPaymentsApiProviderImpl
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

interface StudentsPaymentsActionsInteractor {
    fun addPayment(newStudentPayment: NewStudentPayment): SmartPromise<Unit, Exception>
}

@EBean
open class StudentsPaymentsActionsInteractorImpl : StudentsPaymentsActionsInteractor {
    @Bean
    lateinit var studentsPaymentsApiProvider: StudentsPaymentsApiProviderImpl

    @Bean
    lateinit var authService: AuthStorageInteractorImpl

    @Bean(StudentsPaymentsModifierServiceImpl::class)
    lateinit var studentsPaymentsModifierService: StudentsPaymentsModifierService

    @Bean(StudentsPaymentsProviderServiceImpl::class)
    lateinit var studentsPaymentsProviderService: StudentsPaymentsProviderService

    override fun addPayment(newStudentPayment: NewStudentPayment): SmartPromise<Unit, Exception> {
        return smartTask {
            val studentPaymentId = studentsPaymentsApiProvider
                .provide()
                .addStudentPayment(newStudentPayment)
                .execute()
                .body()!!

            studentsPaymentsModifierService.add(
                    payment = ExistingStudentPayment(
                            id = studentPaymentId,
                            info = newStudentPayment.info,
                            processed = false
                    )
            )
        }
    }

    fun setPaymentProcessed(paymentId: Long): SmartPromise<ExistingStudentPayment, Exception> {
        return smartTask {
            val oldStudentPayment = studentsPaymentsProviderService.getByPaymentId(
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

            studentsPaymentsModifierService.add(payment = newStudentPayment)

            return@smartTask newStudentPayment
        }
    }
}
