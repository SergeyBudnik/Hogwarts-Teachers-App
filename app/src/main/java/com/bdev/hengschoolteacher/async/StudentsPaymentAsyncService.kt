package com.bdev.hengschoolteacher.async

import com.bdev.hengschoolteacher.async.common.SmartPromise
import com.bdev.hengschoolteacher.async.common.SmartTask.Companion.smartTask
import com.bdev.hengschoolteacher.data.school.student.StudentPayment
import com.bdev.hengschoolteacher.rest.StudentsPaymentsRest
import com.bdev.hengschoolteacher.service.AuthService
import com.bdev.hengschoolteacher.service.StudentsPaymentsService
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean
import org.androidannotations.rest.spring.annotations.RestService

@EBean
open class StudentsPaymentAsyncService : CommonAsyncService() {
    @RestService
    lateinit var studentsPaymentsRest: StudentsPaymentsRest

    @Bean
    lateinit var authService: AuthService

    @Bean
    lateinit var studentsPaymentsService: StudentsPaymentsService

    fun addPayment(payment: StudentPayment): SmartPromise<Unit, Exception> {
        return smartTask {
            authenticateAll(
                    listOf(studentsPaymentsRest),
                    authService.getAuthInfo()
            )

            studentsPaymentsRest.addStudentPayment(payment)

            studentsPaymentsService.addPayment(payment)
        }
    }
}
