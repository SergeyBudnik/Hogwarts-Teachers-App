package com.bdev.hengschoolteacher.service

import com.bdev.hengschoolteacher.data.school.DayOfWeek
import com.bdev.hengschoolteacher.data.school.group.Lesson
import com.bdev.hengschoolteacher.data.school.lesson.LessonStatus
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

    fun getIncome(teacherId: Long): Int {
        var income = 0

        for (dayOfWeek in DayOfWeek.values()) {
            income += getDailyIncome(teacherId, dayOfWeek)
        }

        return income
    }

    fun getDailyIncome(teacherId: Long, dayOfWeek: DayOfWeek): Int {
        val lessons = lessonsService.getTeacherLessons(teacherId)[dayOfWeek] ?: emptyList()

        var payment = 0

        var previousLesson: Lesson? = null

        val passedLessons = lessons
                .asSequence()
                .map { it.lesson }
                .filter {
                    val lessonStatus = lessonStatusService.getLessonStatus(it.id, lessonsService.getLessonStartTime(it.id))

                    lessonStatus?.type == LessonStatus.Type.FINISHED
                }.toList()

        for (lesson in passedLessons) {
            if (previousLesson == null) {
                payment += PaymentService.HALF_HOUR_PRICE
            } else if (lesson.startTime.order - previousLesson.finishTime.order > 2) {
                payment += PaymentService.HALF_HOUR_PRICE
            }

            payment += 3 * PaymentService.HALF_HOUR_PRICE * (lesson.finishTime.order - lesson.startTime.order) / 2

            previousLesson = lesson
        }

        return payment
    }
}
