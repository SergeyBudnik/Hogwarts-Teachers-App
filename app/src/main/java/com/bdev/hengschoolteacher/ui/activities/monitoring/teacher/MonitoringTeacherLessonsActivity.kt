package com.bdev.hengschoolteacher.ui.activities.monitoring.teacher

import android.annotation.SuppressLint
import android.view.View
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.service.LessonStateService
import com.bdev.hengschoolteacher.service.LessonsService
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
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

        fun redirectToChild(current: BaseActivity, teacherId: Long) {
            redirect(current = current, teacherId = teacherId)
                    .withAnim(R.anim.slide_open_enter, R.anim.slide_open_exit)
                    .go()
        }

        fun redirectToSibling(current: BaseActivity, teacherId: Long) {
            redirect(current = current, teacherId = teacherId)
                    .goAndCloseCurrent()
        }

        private fun redirect(current: BaseActivity, teacherId: Long): RedirectBuilder {
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
    lateinit var lessonStateService: LessonStateService

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

        monitoringTeacherLessonsListView.bind(showTeacher = false)

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
            !filterEnabled || !lessonStateService.isLessonFilled(it.lesson, weekIndex)
        }

        monitoringTeacherLessonsListView.fill(lessons, weekIndex)
    }

    private fun toggleFilter() {
        filterEnabled = !filterEnabled

        monitoringTeacherLessonsHeaderView.setFirstRightButtonColor(getHeaderButtonColor(filterEnabled))

        initLessonsList()
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
