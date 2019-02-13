package com.bdev.hengschoolteacher.service

import com.bdev.hengschoolteacher.data.school.DayOfWeek
import com.bdev.hengschoolteacher.data.school.group.Group
import com.bdev.hengschoolteacher.data.school.group.Lesson
import com.bdev.hengschoolteacher.data.school.Time
import com.bdev.hengschoolteacher.data.school.group.GroupAndLesson
import com.bdev.hengschoolteacher.utils.TimeUtils
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean
import java.util.*

@EBean
open class LessonsService {
    @Bean
    lateinit var groupsService: GroupsService

    fun getLesson(lessonId: Long): Lesson? {
        for (group in groupsService.getGroups()) {
            for (lesson in group.lessons) {
                if (lesson.id == lessonId) {
                    return lesson
                }
            }
        }

        return null
    }

    fun getAllLessons(): Map<DayOfWeek, List<GroupAndLesson>> {
        return getLessonsByCondition { true }
    }

    fun getLessonGroup(lessonId: Long) : Group? {
        return groupsService
                .getGroups()
                .find { group -> group.lessons.asSequence().map { lesson -> lesson.id }.contains(lessonId) }
    }

    fun getTeacherLessons(teacherId: Long): Map<DayOfWeek, List<GroupAndLesson>> {
        return getLessonsByCondition { it.teacherId == teacherId }
    }

    private fun getLessonsByCondition(condition: (Lesson) -> Boolean): Map<DayOfWeek, List<GroupAndLesson>> {
        val lessons = HashMap<DayOfWeek, MutableList<GroupAndLesson>>()

        DayOfWeek.values().forEach { lessons[it] = ArrayList() }

        val dayLessons: (DayOfWeek) -> MutableList<GroupAndLesson> = { lessons[it] ?: throw RuntimeException() }

        groupsService
                .getGroups()
                .forEach { group ->
                    group.lessons
                            .filter(condition)
                            .forEach { lesson -> dayLessons.invoke(lesson.day).add(GroupAndLesson(group, lesson)) }
                }

        DayOfWeek.values().forEach {
            lessons[it] = dayLessons
                    .invoke(it)
                    .asSequence()
                    .sortedBy { lesson -> lesson.lesson.startTime.order }
                    .toMutableList()
        }

        return lessons
    }

    fun getLessonsAmountInMonth(groupId: Long, month: Int): Int {
        val calendar = Calendar.getInstance()

        with (calendar) {
            set(Calendar.MONTH, month)
            set(Calendar.DAY_OF_MONTH, 0)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE,      0)
            set(Calendar.SECOND,      0)
            set(Calendar.MILLISECOND, 0)
        }

        val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        val group = groupsService.getGroup(groupId) ?: throw RuntimeException()

        var lessonsAmount = 0

        for (i in 1..daysInMonth) {
            calendar.set(Calendar.DAY_OF_MONTH, i)

            val calendarDayOfWeek = TimeUtils().getDayOfWeek(calendar)

            for (lesson in group.lessons) {
                if (lesson.day == calendarDayOfWeek) {
                    lessonsAmount++
                }
            }
        }

        return lessonsAmount
    }

    fun isLessonFinished(lesson: Lesson): Boolean {
        return Date(getLessonFinishTime(lesson.id)).before(Date())
    }

    fun getLessonStartTime(lessonId: Long): Long {
        val lesson = getLesson(lessonId) ?: throw RuntimeException()

        return getLessonTime(lesson.day, lesson.startTime)
    }

    fun getLessonFinishTime(lessonId: Long): Long {
        val lesson = getLesson(lessonId) ?: throw RuntimeException()

        return getLessonTime(lesson.day, lesson.finishTime)
    }

    private fun getLessonTime(day: DayOfWeek, time: Time): Long {
        val calendar = Calendar.getInstance()

        calendar.set(Calendar.DAY_OF_WEEK, TimeUtils().getCalendayDayOfWeek(day))
        calendar.set(Calendar.HOUR_OF_DAY, TimeUtils().getCalendarHour(time))
        calendar.set(Calendar.MINUTE,      TimeUtils().getCalendarMinute(time))
        calendar.set(Calendar.SECOND,      0)
        calendar.set(Calendar.MILLISECOND, 0)

        return calendar.timeInMillis
    }
}
