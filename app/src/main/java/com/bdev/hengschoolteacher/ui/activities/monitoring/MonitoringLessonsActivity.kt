package com.bdev.hengschoolteacher.ui.activities.monitoring

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.view.View
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.service.LessonStatusService
import com.bdev.hengschoolteacher.service.LessonsAttendancesService
import com.bdev.hengschoolteacher.service.LessonsService
import com.bdev.hengschoolteacher.service.StudentsService
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import com.bdev.hengschoolteacher.ui.activities.lesson.LessonActivity
import com.bdev.hengschoolteacher.ui.activities.lesson.LessonActivity_
import com.bdev.hengschoolteacher.ui.utils.HeaderElementsUtils
import com.bdev.hengschoolteacher.ui.utils.RedirectBuilder
import com.bdev.hengschoolteacher.ui.views.app.AppMenuView
import com.bdev.hengschoolteacher.ui.views.app.monitoring.MonitoringHeaderView
import kotlinx.android.synthetic.main.activity_monitoring_lessons.*
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EActivity

@SuppressLint("Registered")
@EActivity(R.layout.activity_monitoring_lessons)
open class MonitoringLessonsActivity : BaseActivity() {
    companion object {
        const val REQUEST_CODE_LESSON = 1
    }

    @Bean
    lateinit var lessonsService: LessonsService
    @Bean
    lateinit var studentsService: StudentsService
    @Bean
    lateinit var lessonStatusService: LessonStatusService
    @Bean
    lateinit var lessonsAttendancesService: LessonsAttendancesService

    private var filterEnabled = true
    private var calendarEnabled = false

    private var weekIndex = 0

    @AfterViews
    fun init() {
        monitoringLessonsHeaderView
                .setLeftButtonAction { monitoringLessonsMenuLayoutView.openMenu() }
                .setFirstRightButtonAction { toggleFilter() }
                .setFirstRightButtonColor(getHeaderButtonColor(filterEnabled))
                .setSecondRightButtonAction { toggleCalendar() }
                .setSecondRightButtonColor(getHeaderButtonColor(calendarEnabled))

        monitoringLessonsSecondaryHeaderView.bind(currentItem = MonitoringHeaderView.Item.LESSONS)

        monitoringLessonsMenuLayoutView.setCurrentMenuItem(AppMenuView.Item.MONITORING)

        monitoringLessonsListView.bind(
                showTeacher = true,
                clickAction = {
                    group, lesson, weekIndex -> RedirectBuilder.redirect(this)
                        .to(LessonActivity_::class.java)
                        .withExtra(LessonActivity.EXTRA_GROUP_ID, group.id)
                        .withExtra(LessonActivity.EXTRA_LESSON_ID, lesson.id)
                        .withExtra(LessonActivity.EXTRA_WEEK_INDEX, weekIndex)
                        .withAnim(R.anim.slide_open_enter, R.anim.slide_open_exit)
                        .goForResult(REQUEST_CODE_LESSON)
                }
        )

        monitoringLessonsWeekSelectionBarView.init { weekIndex ->
            this.weekIndex = weekIndex

            initLessonsList()
        }

        monitoringLessonsNoLessonsView.bind {
            toggleFilter()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_LESSON) {
            if (resultCode == Activity.RESULT_OK) {
                initLessonsList()
            }
        }
    }

    private fun initLessonsList() {
        val lessons = lessonsService.getAllLessons()
                .filter {
                    val attendanceFilled = lessonsAttendancesService
                            .isLessonAttendanceFilled(
                                    lessonId = it.lesson.id,
                                    weekIndex = weekIndex
                            )

                    val statusFilled = lessonStatusService.getLessonStatus(
                            lessonId = it.lesson.id,
                            lessonTime = lessonsService.getLessonStartTime(it.lesson.id, weekIndex)
                    ) != null

                    return@filter if (filterEnabled) {
                        !attendanceFilled || !statusFilled
                    } else {
                        true
                    }
                }

        monitoringLessonsListView.setLessons(lessons)
        monitoringLessonsListView.setWeekIndex(weekIndex)

        monitoringLessonsNoLessonsView.visibility = if (lessons.isEmpty() && filterEnabled) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    private fun toggleFilter() {
        filterEnabled = !filterEnabled

        monitoringLessonsHeaderView.setFirstRightButtonColor(getHeaderButtonColor(filterEnabled))

        initLessonsList()
    }

    private fun toggleCalendar() {
        calendarEnabled = !calendarEnabled

        monitoringLessonsHeaderView.setSecondRightButtonColor(getHeaderButtonColor(calendarEnabled))

        monitoringLessonsWeekSelectionBarView.visibility = if (calendarEnabled) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    private fun getHeaderButtonColor(enabled: Boolean): Int {
        return HeaderElementsUtils.getColor(this, enabled)
    }
}
