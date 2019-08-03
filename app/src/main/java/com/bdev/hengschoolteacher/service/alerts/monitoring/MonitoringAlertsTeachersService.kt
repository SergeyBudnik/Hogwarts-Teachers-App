package com.bdev.hengschoolteacher.service.alerts.monitoring

import com.bdev.hengschoolteacher.service.StudentsPaymentsService
import com.bdev.hengschoolteacher.service.teacher.TeacherStorageService
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

@EBean
open class MonitoringAlertsTeachersService {
    @Bean
    lateinit var teacherStorageService: TeacherStorageService
    @Bean
    lateinit var studentsPaymentsService: StudentsPaymentsService

    fun haveAlerts(): Boolean {
        return teacherStorageService
                .getAllTeachers()
                .map { studentsPaymentsService.getPaymentsToTeacher(it.id, true).isNotEmpty() }
                .fold(false) { res, notEmpty -> res || notEmpty }
    }
}
