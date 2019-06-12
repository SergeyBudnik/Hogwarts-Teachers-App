package com.bdev.hengschoolteacher.service.teacher

import com.bdev.hengschoolteacher.data.school.student_payment.StudentPayment
import com.bdev.hengschoolteacher.service.StudentsPaymentsService
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

@EBean
open class TeacherPaymentsService {
    @Bean
    lateinit var studentsPaymentsService: StudentsPaymentsService

    fun getPayments(teacherId: Long, onlyUnprocessed: Boolean): List<StudentPayment> {
        return studentsPaymentsService
                .getAllPayments()
                .filter { it.teacherId == teacherId }
                .filter { !onlyUnprocessed || !it.processed }
                .toList()
    }
}
