package com.bdev.hengschoolteacher.service

import com.bdev.hengschoolteacher.data.school.teacher.TeacherActionType
import com.bdev.hengschoolteacher.data.school.teacher.TeacherPayment
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

@EBean
open class TeachersPaymentService {
    companion object {
        const val TEACHER_HALF_HOUR_RATE = 180
    }

    @Bean
    lateinit var lessonsService: LessonsService
    @Bean
    lateinit var lessonStatusService: LessonStatusService

    @Bean
    lateinit var teacherActionsService: TeacherActionsService

    fun getTeacherPayments(teacherId: Long): List<TeacherPayment> {
        val teacherPayments = ArrayList<TeacherPayment>()

        val teacherActions = teacherActionsService.getTeacherActions(teacherId)

        for (teacherAction in teacherActions) {
            when (teacherAction.type) {
                TeacherActionType.ROAD -> teacherPayments.add(TeacherPayment(
                        TEACHER_HALF_HOUR_RATE,
                        teacherAction
                ))

                TeacherActionType.LESSON -> teacherPayments.add(TeacherPayment(
                        3 * TEACHER_HALF_HOUR_RATE * (teacherAction.finishTime.order - teacherAction.startTime.order) / 2,
                        teacherAction
                ))
            }
        }

        return teacherPayments
    }
}
