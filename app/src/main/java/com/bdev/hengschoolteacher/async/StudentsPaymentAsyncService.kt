package com.bdev.hengschoolteacher.async

import com.bdev.hengschoolteacher.async.common.SmartPromise
import com.bdev.hengschoolteacher.async.common.SmartTask.Companion.smartTask
import com.bdev.hengschoolteacher.data.school.student_payment.ExistingStudentPayment
import com.bdev.hengschoolteacher.data.school.student_payment.NewStudentPayment
import com.bdev.hengschoolteacher.rest.StudentsPaymentsRest
import com.bdev.hengschoolteacher.services.auth.AuthService
import com.bdev.hengschoolteacher.services.students_payments.*
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean
import org.androidannotations.rest.spring.annotations.RestService
import java.lang.RuntimeException

@EBean
open class StudentsPaymentAsyncService : CommonAsyncService() {
    @RestService
    lateinit var studentsPaymentsRest: StudentsPaymentsRest

    @Bean
    lateinit var authService: AuthService

    @Bean(StudentsPaymentsModifierServiceImpl::class)
    lateinit var studentsPaymentsModifierService: StudentsPaymentsModifierService

    @Bean(StudentsPaymentsProviderServiceImpl::class)
    lateinit var studentsPaymentsProviderService: StudentsPaymentsProviderService

    fun addPayment(newStudentPayment: NewStudentPayment): SmartPromise<Unit, Exception> {
        return smartTask {
            authenticateAll(
                    listOf(studentsPaymentsRest),
                    authService.getAuthInfo()
            )

            val studentPaymentId = studentsPaymentsRest.addStudentPayment(newStudentPayment)!!

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

            authenticateAll(
                    rests = listOf(studentsPaymentsRest),
                    authInfo = authService.getAuthInfo()
            )

            studentsPaymentsRest.setStudentPaymentsProcessed(
                    paymentId = paymentId,
                    processed = !oldStudentPayment.processed
            )

            val newStudentPayment = oldStudentPayment.copy(
                    processed = !oldStudentPayment.processed
            )

            studentsPaymentsModifierService.add(payment = newStudentPayment)

            return@smartTask newStudentPayment
        }
    }
}
