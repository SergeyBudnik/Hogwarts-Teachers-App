package com.bdev.hengschoolteacher.interactors.lessons

import com.bdev.hengschoolteacher.data.school.DayOfWeek
import com.bdev.hengschoolteacher.data.school.Time
import com.bdev.hengschoolteacher.data.school.group.Group
import com.bdev.hengschoolteacher.data.school.group.GroupAndLesson
import com.bdev.hengschoolteacher.data.school.group.Lesson
import com.bdev.hengschoolteacher.data.school.student.Student
import com.bdev.hengschoolteacher.data.school.student.StudentGroup
import com.bdev.hengschoolteacher.interactors.groups.GroupsStorageInteractor
import com.bdev.hengschoolteacher.interactors.groups.GroupsStorageInteractorImpl
import com.bdev.hengschoolteacher.interactors.students.StudentsStorageInteractor
import com.bdev.hengschoolteacher.interactors.students.StudentsStorageInteractorImpl
import com.bdev.hengschoolteacher.utils.TimeUtils
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean
import java.util.*
import kotlin.collections.ArrayList

interface LessonsInteractor {
    fun getLesson(lessonId: Long): GroupAndLesson?
    fun getAllStudentLessonsInMonth(studentLogin: String, month: Int): List<Pair<GroupAndLesson, Long>>
    fun getAllLessons(weekIndex: Int): List<GroupAndLesson>
    fun getLessonStudents(lessonId: Long, weekIndex: Int): List<Student>
    fun getTeacherLessons(teacherLogin: String, weekIndex: Int): List<GroupAndLesson>
    fun getStudentLessons(studentLogin: String): List<GroupAndLesson>
    fun getLessonsAmountInMonthSinceTime(groupId: Long, time: Long): Int
    fun getLessonsAmountInMonth(groupId: Long, month: Int): Int
    fun getLessonStartTime(lessonId: Long, weekIndex: Int): Long
    fun getLessonFinishTime(lessonId: Long, weekIndex: Int): Long
}

@EBean
open class LessonsInteractorImpl : LessonsInteractor {
    @Bean(GroupsStorageInteractorImpl::class)
    lateinit var groupsStorageInteractor: GroupsStorageInteractor
    @Bean(StudentsStorageInteractorImpl::class)
    lateinit var studentsStorageInteractor: StudentsStorageInteractor

    override fun getLesson(lessonId: Long): GroupAndLesson? {
        for (group in groupsStorageInteractor.getAll()) {
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

    override fun getAllStudentLessonsInMonth(studentLogin: String, month: Int): List<Pair<GroupAndLesson, Long>> {
        val lessons = ArrayList<Pair<GroupAndLesson, Long>>()

        val student = studentsStorageInteractor.getByLogin(studentLogin)

        student?.studentGroups?.forEach { studentGroup ->
            val group = groupsStorageInteractor.getById(groupId = studentGroup.groupId)

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

    override fun getAllLessons(weekIndex: Int): List<GroupAndLesson> {
        return getLessonsByCondition { _, lesson -> lessonTimeMatches(lesson, weekIndex) }
    }

    override fun getLessonStudents(lessonId: Long, weekIndex: Int): List<Student> {
        val groupAndLesson = getLesson(lessonId) ?: return emptyList()

        val lessonStartTime = getLessonStartTime(lessonId, weekIndex)
        val lessonFinishTime = getLessonFinishTime(lessonId, weekIndex)

        return studentsStorageInteractor
                .getAll()
                .filter { student -> student.studentGroups
                        .filter { it.groupId == groupAndLesson.group.id }
                        .filter { it.startTime < lessonStartTime }
                        .filter { lessonFinishTime < it.finishTime }
                        .any()
                }
    }

    override fun getTeacherLessons(teacherLogin: String, weekIndex: Int): List<GroupAndLesson> {
        return getLessonsByCondition { _, lesson ->
            val teacherMatches = lesson.teacherLogin == teacherLogin
            val timeMatches = lessonTimeMatches(lesson, weekIndex)

            teacherMatches && timeMatches
        }
    }

    // ToDo: add info about time
    override fun getStudentLessons(studentLogin: String): List<GroupAndLesson> {
        val student = studentsStorageInteractor.getByLogin(studentLogin) ?: throw RuntimeException()

        return getLessonsByCondition { group, _ -> student.studentGroups.map { it.groupId }.contains(group.id) }
    }

    private fun getLessonsByCondition(condition: (Group, Lesson) -> Boolean): List<GroupAndLesson> {
        val lessons = ArrayList<GroupAndLesson>()

        groupsStorageInteractor
                .getAll()
                .forEach { group ->
                    group.lessons
                            .filter { lesson -> condition.invoke(group, lesson) }
                            .forEach { lesson -> lessons.add(GroupAndLesson(group, lesson)) }
                }

        return lessons
    }

    override fun getLessonsAmountInMonthSinceTime(groupId: Long, time: Long): Int {
        val monthCalendar = getCalendarSinceCurrentMonthStart()
        val timeCalendar = getCalendarSinceTime(time = time)

        val calendar = if (monthCalendar.time.time < timeCalendar.time.time) {
            monthCalendar
        } else {
            timeCalendar
        }

        val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        val group = groupsStorageInteractor.getById(groupId) ?: throw RuntimeException()

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

    override fun getLessonsAmountInMonth(groupId: Long, month: Int): Int {
        val calendar = getCalendarSinceMonthStart(month = month)

        val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        val group = groupsStorageInteractor.getById(groupId) ?: throw RuntimeException()

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

    override fun getLessonStartTime(lessonId: Long, weekIndex: Int): Long {
        val groupAndLesson = getLesson(lessonId) ?: throw RuntimeException()

        return getLessonTime(
                day = groupAndLesson.lesson.day,
                time = groupAndLesson.lesson.startTime,
                weekIndex = weekIndex
        )
    }

    override fun getLessonFinishTime(lessonId: Long, weekIndex: Int): Long {
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
        val deactivationTimeMatches = lessonStartTime <= lesson.deactivationTime

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
