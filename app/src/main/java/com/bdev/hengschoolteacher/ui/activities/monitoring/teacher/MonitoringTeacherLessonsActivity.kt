package com.bdev.hengschoolteacher.ui.activities.monitoring.teacher

import android.annotation.SuppressLint
import android.view.View
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.service.LessonStateService
import com.bdev.hengschoolteacher.service.LessonsService
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import com.bdev.hengschoolteacher.ui.utils.RedirectBuilder
import com.bdev.hengschoolteacher.ui.views.app.AppLayoutView
import com.bdev.hengschoolteacher.ui.views.app.monitoring.teacher.MonitoringTeacherHeaderView
import kotlinx.android.synthetic.main.activity_monitoring_teacher_lessons.*
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EActivity
import org.androidannotations.annotations.Extra

@SuppressLint("Registered")
@EActivity(R.layout.activity_monitoring_teacher_lessons)
open class MonitoringTeacherLessonsActivity : BaseActivity() {
    companion object {
        const val EXTRA_TEACHER_LOGIN = "EXTRA_TEACHER_LOGIN"

        fun redirectToChild(current: BaseActivity, teacherLogin: String) {
            redirect(current = current, teacherLogin = teacherLogin)
                    .withAnim(R.anim.slide_open_enter, R.anim.slide_open_exit)
                    .go()
        }

        fun redirectToSibling(current: BaseActivity, teacherLogin: String) {
            redirect(current = current, teacherLogin = teacherLogin)
                    .goAndCloseCurrent()
        }

        private fun redirect(current: BaseActivity, teacherLogin: String): RedirectBuilder {
            return RedirectBuilder
                    .redirect(current)
                    .to(MonitoringTeacherLessonsActivity_::class.java)
                    .withExtra(EXTRA_TEACHER_LOGIN, teacherLogin)
        }
    }

    @Extra(EXTRA_TEACHER_LOGIN)
    lateinit var teacherLogin: String

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
                .setFirstRightButtonActive(filterEnabled)
                .setSecondRightButtonAction { toggleCalendar() }
                .setSecondRightButtonActive(calendarEnabled)

        monitoringTeacherLessonsSecondaryHeaderView.bind(
                currentItem = MonitoringTeacherHeaderView.Item.LESSONS,
                teacherLogin = teacherLogin
        )

        monitoringTeacherLessonsTeacherInfoView.bind(
                teacherLogin = teacherLogin
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
        val lessons = lessonsService
                .getTeacherLessons(teacherLogin = teacherLogin, weekIndex = weekIndex)
                .filter {
                    !filterEnabled || !lessonStateService.isLessonFilled(it.lesson, weekIndex)
                }

        monitoringTeacherLessonsListView.fill(lessons, weekIndex)
    }

    private fun toggleFilter() {
        filterEnabled = !filterEnabled

        monitoringTeacherLessonsHeaderView.setFirstRightButtonActive(filterEnabled)

        initLessonsList()
    }

    private fun toggleCalendar() {
        calendarEnabled = !calendarEnabled

        monitoringTeacherLessonsHeaderView.setSecondRightButtonActive(calendarEnabled)

        monitoringTeacherLessonsWeekSelectionBarView.visibility = if (calendarEnabled) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    override fun getAppLayoutView(): AppLayoutView? {
        return null
    }

}
