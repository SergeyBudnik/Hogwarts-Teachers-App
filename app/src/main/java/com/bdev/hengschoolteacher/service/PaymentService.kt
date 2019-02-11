package com.bdev.hengschoolteacher.service

import com.bdev.hengschoolteacher.data.school.DayOfWeek
import com.bdev.hengschoolteacher.data.school.Time
import com.bdev.hengschoolteacher.data.school.group.Group
import com.bdev.hengschoolteacher.data.school.group.Lesson
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

@EBean
open class PaymentService {
    companion object {
        const val HALF_HOUR_PRICE = 175
    }

    @Bean
    lateinit var lessonsService: LessonsService

    fun getPayment(teacherId: Long): Map<DayOfWeek, Int> {
        val payment = HashMap<DayOfWeek, Int>()
        val lessons = lessonsService.getTeacherLessons(teacherId)

        lessons.forEach{
            (dayOfWeek, dayLessons) -> payment[dayOfWeek] = getDayPayment(dayLessons)
        }

        return payment
    }

    private fun getDayPayment(dayLessons: List<Pair<Group, Lesson>>): Int {
        var payment = 0

        var previousLesson: Lesson? = null

        for (lesson in dayLessons.map { it.second }) {
            if (previousLesson == null) {
                payment += HALF_HOUR_PRICE
            } else if (lesson.startTime.order - previousLesson.finishTime.order > 2) {
                payment += HALF_HOUR_PRICE
            }

            payment += 3 * HALF_HOUR_PRICE * (lesson.finishTime.order - lesson.startTime.order) / 2

            previousLesson = lesson
        }

        return payment
    }
}
