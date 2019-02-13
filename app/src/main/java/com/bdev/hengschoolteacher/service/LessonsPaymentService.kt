package com.bdev.hengschoolteacher.service

import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

@EBean
open class LessonsPaymentService {
    @Bean
    lateinit var lessonsService: LessonsService
    @Bean
    lateinit var studentsService: StudentsService
}
