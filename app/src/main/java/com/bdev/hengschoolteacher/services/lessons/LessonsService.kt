package com.bdev.hengschoolteacher.services.lessons

import com.bdev.hengschoolteacher.data.school.DayOfWeek
import com.bdev.hengschoolteacher.data.school.Time
import com.bdev.hengschoolteacher.data.school.group.Group
import com.bdev.hengschoolteacher.data.school.group.GroupAndLesson
import com.bdev.hengschoolteacher.data.school.group.Lesson
import com.bdev.hengschoolteacher.data.school.student.Student
import com.bdev.hengschoolteacher.data.school.student.StudentGroup
import com.bdev.hengschoolteacher.services.groups.GroupsStorageService
import com.bdev.hengschoolteacher.services.groups.GroupsStorageServiceImpl
import com.bdev.hengschoolteacher.services.students.StudentsStorageService
import com.bdev.hengschoolteacher.services.students.StudentsStorageServiceImpl
import com.bdev.hengschoolteacher.utils.TimeUtils
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean
import java.util.*
import kotlin.collections.ArrayList

@EBean
open class LessonsService {
    @Bean(GroupsStorageServiceImpl::class)
    lateinit var groupsStorageService: GroupsStorageService
    @Bean(StudentsStorageServiceImpl::class)
    lateinit var studentsStorageService: StudentsStorageService

    fun getLesson(lessonId: Long): GroupAndLesson? {
        for (group in groupsStorageService.getAll()) {
            for (lesson in group.lessons) {
                if (lesson.id == lessonId) {
                    return GroupAndLesson(
                            group = group,
                            lesson = lesson
                    )
                }
            }
        }

        return null
    }

    fun getAllStudentLessonsInMonth(studentLogin: String, month: Int): List<Pair<GroupAndLesson, Long>> {
        val lessons = ArrayList<Pair<GroupAndLesson, Long>>()

        val student = studentsStorageService.getByLogin(studentLogin)

        student?.studentGroups?.forEach { studentGroup ->
            val group = groupsStorageService.getById(groupId = studentGroup.groupId)

            group?.lessons?.forEach { lesson ->
                val lessonTimes = getLessonTimesInMonth(
                        lesson = lesson,
                        studentGroup = studentGroup,
                        month = month,
                        startTime = studentGroup.startTime
                )

                lessonTimes.forEach { lessonTime ->
                    lessons.add(
                            Pair(
                                    GroupAndLesson(group, lesson),
                                    lessonTime
                            )
                    )
                }
            }
        }

        return lessons.sortedBy { it.second }
    }

    private fun getLessonTimesInMonth(
            lesson: Lesson,
            studentGroup: StudentGroup,
            month: Int,
            startTime: Long
    ): List<Long> {
        val lessonTimes = ArrayList<Long>()

        val c1 = getCalendarSinceMonthStart(month = month)
        val c2 = getCalendarSinceTime(time = startTime)

        val c = if (c1.time.time > c2.time.time) { c1 } else { c2 }

        if (c.get(Calendar.MONTH) != month) {
            return emptyList()
        } else {
            val daysInMonth = c.getActualMaximum(Calendar.DAY_OF_MONTH)
            val currentDayInMonth = c.get(Calendar.DAY_OF_MONTH)

            for (day in currentDayInMonth..daysInMonth) {
                c.set(Calendar.DAY_OF_MONTH, day)

                val dayOfWeek = TimeUtils().getDayOfWeek(c)

                val lessonCalendar = Calendar.getInstance()

                lessonCalendar.set(Calendar.MONTH, month)
                lessonCalendar.set(Calendar.DAY_OF_MONTH, day)
                lessonCalendar.set(Calendar.HOUR_OF_DAY, TimeUtils().getCalendarHour(lesson.startTime))
                lessonCalendar.set(Calendar.MINUTE,      TimeUtils().getCalendarMinute(lesson.startTime))
                lessonCalendar.set(Calendar.SECOND,      0)
                lessonCalendar.set(Calendar.MILLISECOND, 0)

                val lessonTime = lessonCalendar.time.time

                if (lesson.day == dayOfWeek && lesson.creationTime <= lessonTime && lessonTime <= lesson.deactivationTime && studentGroup.startTime <= lessonTime && lessonTime <= studentGroup.finishTime) {
                    lessonTimes.add(lessonTime)
                }
            }

            return lessonTimes
        }
    }

    fun getAllLessons(weekIndex: Int): List<GroupAndLesson> {
        return getLessonsByCondition { _, lesson -> lessonTimeMatches(lesson, weekIndex) }
    }

    fun getLessonStudents(lessonId: Long, weekIndex: Int): List<Student> {
        val groupAndLesson = getLesson(lessonId) ?: return emptyList()

        val lessonStartTime = getLessonStartTime(lessonId, weekIndex)
        val lessonFinishTime = getLessonFinishTime(lessonId, weekIndex)

        return studentsStorageService
                .getAll()
                .filter { student -> student.studentGroups
                        .filter { it.groupId == groupAndLesson.group.id }
                        .filter { it.startTime < lessonStartTime }
                        .filter { lessonFinishTime < it.finishTime }
                        .any()
                }
    }

    fun getTeacherLessons(teacherLogin: String, weekIndex: Int): List<GroupAndLesson> {
        return getLessonsByCondition { _, lesson ->
            val teacherMatches = lesson.teacherLogin == teacherLogin
            val timeMatches = lessonTimeMatches(lesson, weekIndex)

            teacherMatches && timeMatches
        }
    }

    // ToDo: add info about time
    fun getStudentLessons(studentLogin: String): List<GroupAndLesson> {
        val student = studentsStorageService.getByLogin(studentLogin) ?: throw RuntimeException()

        return getLessonsByCondition { group, _ -> student.studentGroups.map { it.groupId }.contains(group.id) }
    }

    private fun getLessonsByCondition(condition: (Group, Lesson) -> Boolean): List<GroupAndLesson> {
        val lessons = ArrayList<GroupAndLesson>()

        groupsStorageService
                .getAll()
                .forEach { group ->
                    group.lessons
                            .filter { lesson -> condition.invoke(group, lesson) }
                            .forEach { lesson -> lessons.add(GroupAndLesson(group, lesson)) }
                }

        return lessons
    }

    fun getLessonsAmountInMonthSinceTime(groupId: Long, time: Long): Int {
        val monthCalendar = getCalendarSinceCurrentMonthStart()
        val timeCalendar = getCalendarSinceTime(time = time)

        val calendar = if (monthCalendar.time.time < timeCalendar.time.time) {
            monthCalendar
        } else {
            timeCalendar
        }

        val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        val group = groupsStorageService.getById(groupId) ?: throw RuntimeException()

        var lessonsAmount = 0

        val currentDayInMonth = calendar.get(Calendar.DAY_OF_MONTH)

        for (i in currentDayInMonth until (daysInMonth + 1)) {
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

    fun getLessonsAmountInMonth(groupId: Long, month: Int): Int {
        val calendar = getCalendarSinceMonthStart(month = month)

        val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        val group = groupsStorageService.getById(groupId) ?: throw RuntimeException()

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

    fun getLessonStartTime(lessonId: Long, weekIndex: Int): Long {
        val groupAndLesson = getLesson(lessonId) ?: throw RuntimeException()

        return getLessonTime(
                day = groupAndLesson.lesson.day,
                time = groupAndLesson.lesson.startTime,
                weekIndex = weekIndex
        )
    }

    fun getLessonFinishTime(lessonId: Long, weekIndex: Int): Long {
        val groupAndLesson = getLesson(lessonId) ?: throw RuntimeException()

        return getLessonTime(
                day = groupAndLesson.lesson.day,
                time = groupAndLesson.lesson.finishTime,
                weekIndex = weekIndex
        )
    }

    private fun lessonTimeMatches(lesson: Lesson, weekIndex: Int): Boolean {
        val lessonStartTime = getLessonStartTime(lesson.id, weekIndex)

        val creationTimeMatches = lesson.creationTime <= lessonStartTime
        val deactivationTimeMatches = (lesson.deactivationTime == null) || lessonStartTime <= lesson.deactivationTime

        return creationTimeMatches && deactivationTimeMatches
    }

    private fun getLessonTime(day: DayOfWeek, time: Time, weekIndex: Int): Long {
        val calendar = Calendar.getInstance()

        calendar.set(Calendar.WEEK_OF_YEAR, calendar.get(Calendar.WEEK_OF_YEAR) + weekIndex)
        calendar.set(Calendar.DAY_OF_WEEK, TimeUtils().getCalendayDayOfWeek(day))
        calendar.set(Calendar.HOUR_OF_DAY, TimeUtils().getCalendarHour(time))
        calendar.set(Calendar.MINUTE,      TimeUtils().getCalendarMinute(time))
        calendar.set(Calendar.SECOND,      0)
        calendar.set(Calendar.MILLISECOND, 0)

        return calendar.timeInMillis
    }

    private fun getCalendarSinceCurrentMonthStart(): Calendar {
        val calendar = Calendar.getInstance()

        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE,      0)
        calendar.set(Calendar.SECOND,      0)
        calendar.set(Calendar.MILLISECOND, 0)

        return calendar
    }

    private fun getCalendarSinceMonthStart(month: Int): Calendar {
        val calendar = Calendar.getInstance()

        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE,      0)
        calendar.set(Calendar.SECOND,      0)
        calendar.set(Calendar.MILLISECOND, 0)

        return calendar
    }

    private fun getCalendarSinceTime(time: Long): Calendar {
        val calendar = Calendar.getInstance()

        calendar.time = Date(time)

        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE,      0)
        calendar.set(Calendar.SECOND,      0)
        calendar.set(Calendar.MILLISECOND, 0)

        return calendar
    }
}
