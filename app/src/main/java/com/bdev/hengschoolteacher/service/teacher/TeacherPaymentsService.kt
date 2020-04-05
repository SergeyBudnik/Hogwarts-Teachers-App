package com.bdev.hengschoolteacher.service.teacher

import com.bdev.hengschoolteacher.data.school.student_payment.ExistingStudentPayment
import com.bdev.hengschoolteacher.service.StudentsPaymentsService
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

@EBean
open class TeacherPaymentsService {
    @Bean
    lateinit var studentsPaymentsService: StudentsPaymentsService

    fun getPayments(teacherLogin: String, onlyUnprocessed: Boolean): List<ExistingStudentPayment> {
        return studentsPaymentsService
                .getAllPayments()
                .filter { it.info.staffMemberLogin == teacherLogin }
                .filter { !onlyUnprocessed || !it.processed }
                .toList()
    }
}
