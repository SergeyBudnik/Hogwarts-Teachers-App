package com.bdev.hengschoolteacher.ui.activities.monitoring.teacher

import android.annotation.SuppressLint
import android.view.View
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.service.LessonStatusService
import com.bdev.hengschoolteacher.service.LessonsAttendancesService
import com.bdev.hengschoolteacher.service.LessonsService
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import com.bdev.hengschoolteacher.ui.activities.lesson.LessonActivity
import com.bdev.hengschoolteacher.ui.utils.HeaderElementsUtils
import com.bdev.hengschoolteacher.ui.utils.RedirectBuilder
import kotlinx.android.synthetic.main.activity_monitoring_teacher_lessons.*
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EActivity
import org.androidannotations.annotations.Extra

@SuppressLint("Registered")
@EActivity(R.layout.activity_monitoring_teacher_lessons)
open class MonitoringTeacherLessonsActivity : BaseActivity() {
    companion object {
        const val EXTRA_TEACHER_ID = "EXTRA_TEACHER_ID"

        const val REQUEST_CODE_LESSON = 1

        fun redirect(current: BaseActivity, teacherId: Long): RedirectBuilder {
            return RedirectBuilder
                    .redirect(current)
                    .to(MonitoringTeacherLessonsActivity_::class.java)
                    .withExtra(EXTRA_TEACHER_ID, teacherId)
        }
    }

    @Extra(EXTRA_TEACHER_ID)
    @JvmField
    var teacherId: Long = 0L

    @Bean
    lateinit var lessonsService: LessonsService
    @Bean
    lateinit var lessonStatusService: LessonStatusService
    @Bean
    lateinit var lessonsAttendancesService: LessonsAttendancesService

    private var weekIndex = 0

    private var filterEnabled = true
    private var calendarEnabled = false

    @AfterViews
    fun init() {
        monitoringTeacherLessonsHeaderView
                .setLeftButtonAction { doFinish() }
                .setFirstRightButtonAction { toggleFilter() }
                .setFirstRightButtonColor(getHeaderButtonColor(filterEnabled))
                .setSecondRightButtonAction { toggleCalendar() }
                .setSecondRightButtonColor(getHeaderButtonColor(calendarEnabled))

        monitoringTeacherLessonsSecondaryHeaderView.bind(
                teacherId = teacherId
        )

        monitoringTeacherLessonsTeacherInfoView.bind(
                teacherId = teacherId
        )

        monitoringTeacherLessonsListView.bind(
                showTeacher = false,
                clickAction = {
                    group, lesson, weekIndex -> LessonActivity
                        .redirect(
                                context = this,
                                groupId = group.id,
                                lessonId = lesson.id,
                                weekIndex = weekIndex
                        )
                        .withAnim(R.anim.slide_open_enter, R.anim.slide_open_exit)
                        .goForResult(REQUEST_CODE_LESSON)
                }
        )

        monitoringTeacherLessonsWeekSelectionBarView.init { weekIndex ->
            this.weekIndex = weekIndex

            initLessonsList()
        }
    }

    override fun onBackPressed() {
        doFinish()
    }

    private fun doFinish() {
        finish()
        overridePendingTransition(R.anim.slide_close_enter, R.anim.slide_close_exit)
    }

    private fun initLessonsList() {
        val lessons = lessonsService.getTeacherLessons(teacherId).filter {
            val attendanceFilled = lessonsAttendancesService.isLessonAttendanceFilled(
                    lessonId = it.lesson.id,
                    weekIndex = weekIndex
            )

            val statusFilled = lessonStatusService.getLessonStatus(
                    lessonId = it.lesson.id,
                    lessonTime = lessonsService.getLessonStartTime(it.lesson.id, weekIndex)
            ) != null

            !filterEnabled || !attendanceFilled || !statusFilled
        }

        monitoringTeacherLessonsListView.setLessons(lessons)
        monitoringTeacherLessonsListView.setWeekIndex(weekIndex)
    }

    private fun toggleFilter() {
        filterEnabled = !filterEnabled

        monitoringTeacherLessonsHeaderView.setFirstRightButtonColor(getHeaderButtonColor(filterEnabled))
    }

    private fun toggleCalendar() {
        calendarEnabled = !calendarEnabled

        monitoringTeacherLessonsHeaderView.setSecondRightButtonColor(getHeaderButtonColor(calendarEnabled))

        monitoringTeacherLessonsWeekSelectionBarView.visibility = if (calendarEnabled) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    private fun getHeaderButtonColor(enabled: Boolean): Int {
        return HeaderElementsUtils.getColor(this, enabled)
    }
}
