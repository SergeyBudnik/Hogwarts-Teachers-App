package com.bdev.hengschoolteacher.async

import com.bdev.hengschoolteacher.async.common.SmartPromise
import com.bdev.hengschoolteacher.async.common.SmartTask.Companion.smartTask
import com.bdev.hengschoolteacher.data.school.student_payment.StudentPayment
import com.bdev.hengschoolteacher.data.school.student_payment.StudentPaymentInfo
import com.bdev.hengschoolteacher.rest.StudentsPaymentsRest
import com.bdev.hengschoolteacher.service.AuthService
import com.bdev.hengschoolteacher.service.StudentsPaymentsService
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

    @Bean
    lateinit var studentsPaymentsService: StudentsPaymentsService

    fun addPayment(studentPaymentInfo: StudentPaymentInfo): SmartPromise<Unit, Exception> {
        return smartTask {
            authenticateAll(
                    listOf(studentsPaymentsRest),
                    authService.getAuthInfo()
            )

            val studentPaymentId = studentsPaymentsRest.addStudentPayment(studentPaymentInfo)!!

            studentsPaymentsService.addPayment(
                    StudentPayment(
                            id = studentPaymentId,
                            amount = studentPaymentInfo.amount,
                            studentId = studentPaymentInfo.studentId,
                            staffMemberLogin = studentPaymentInfo.staffMemberLogin,
                            time = studentPaymentInfo.time,
                            processed = studentPaymentInfo.processed
                    )
            )
        }
    }

    fun setPaymentProcessed(paymentId: Long): SmartPromise<StudentPayment, Exception> {
        return smartTask {
            val oldStudentPayment = studentsPaymentsService.getPayment(paymentId) ?: throw RuntimeException()

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

            studentsPaymentsService.addPayment(newStudentPayment)

            return@smartTask newStudentPayment
        }
    }
}
