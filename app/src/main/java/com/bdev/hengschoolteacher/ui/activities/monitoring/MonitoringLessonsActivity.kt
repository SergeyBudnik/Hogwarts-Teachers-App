package com.bdev.hengschoolteacher.ui.activities.monitoring

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.services.LessonStateService
import com.bdev.hengschoolteacher.services.LessonStatusService
import com.bdev.hengschoolteacher.services.LessonsAttendancesService
import com.bdev.hengschoolteacher.services.alerts.monitoring.AlertsMonitoringService
import com.bdev.hengschoolteacher.services.groups.GroupStudentsProviderService
import com.bdev.hengschoolteacher.services.groups.GroupStudentsProviderServiceImpl
import com.bdev.hengschoolteacher.services.groups.GroupsStorageService
import com.bdev.hengschoolteacher.services.lessons.LessonsService
import com.bdev.hengschoolteacher.services.staff.StaffMembersStorageService
import com.bdev.hengschoolteacher.services.students_attendances.StudentsAttendancesProviderService
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import com.bdev.hengschoolteacher.ui.utils.RedirectBuilder
import com.bdev.hengschoolteacher.ui.utils.ViewVisibilityUtils.visibleElseGone
import com.bdev.hengschoolteacher.ui.views.app.AppLayoutView
import com.bdev.hengschoolteacher.ui.views.app.AppMenuView
import com.bdev.hengschoolteacher.ui.views.app.lesson.LessonRowViewData
import com.bdev.hengschoolteacher.ui.views.app.lessons.LessonItemView
import com.bdev.hengschoolteacher.ui.views.app.lessons.LessonsViewData
import com.bdev.hengschoolteacher.ui.views.app.monitoring.MonitoringHeaderView
import kotlinx.android.synthetic.main.activity_monitoring_lessons.*
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EActivity

@SuppressLint("Registered")
@EActivity(R.layout.activity_monitoring_lessons)
open class MonitoringLessonsActivity : BaseActivity() {
    companion object {
        fun redirectToSibling(current: BaseActivity) {
            RedirectBuilder
                    .redirect(current)
                    .to(MonitoringLessonsActivity_::class.java)
                    .goAndCloseCurrent()
        }
    }

    @Bean
    lateinit var lessonsService: LessonsService
    @Bean
    lateinit var lessonStateService: LessonStateService
    @Bean
    lateinit var alertsMonitoringService: AlertsMonitoringService
    @Bean
    lateinit var staffMembersStorageService: StaffMembersStorageService
    @Bean
    lateinit var studentsAttendancesProviderService: StudentsAttendancesProviderService
    @Bean
    lateinit var lessonsAttendancesService: LessonsAttendancesService
    @Bean
    lateinit var lessonStatusService: LessonStatusService

    private var filterEnabled = true
    private var calendarEnabled = false

    private var weekIndex = 0

    @AfterViews
    fun init() {
        initHeader()

        monitoringLessonsSecondaryHeaderView.bind(
                currentItem = MonitoringHeaderView.Item.LESSONS,
                hasLessonsAlert = alertsMonitoringService.lessonsHaveAlerts(),
                hasStudentsAlert = alertsMonitoringService.studentsHaveAlerts(),
                hasTeachersAlert = alertsMonitoringService.teachersHaveAlerts()
        )

        monitoringLessonsMenuLayoutView.setCurrentMenuItem(AppMenuView.Item.MONITORING)

        monitoringLessonsListView.bind()

        monitoringLessonsWeekSelectionBarView.init { weekIndex ->
            this.weekIndex = weekIndex

            initLessonsList()
        }

        monitoringLessonsNoLessonsView.bind {
            toggleFilter()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == LessonItemView.REQUEST_CODE_LESSON) {
            if (resultCode == Activity.RESULT_OK) {
                initLessonsList()
            }
        }
    }

    private fun initHeader() {
        monitoringLessonsHeaderView
                .setLeftButtonAction { monitoringLessonsMenuLayoutView.openMenu() }

        monitoringLessonsHeaderView.getFirstButtonHandler()
                .setAction(action = { toggleFilter() })
                .setToggled(toggled = filterEnabled)

        monitoringLessonsHeaderView.getSecondButtonHandler()
                .setAction(action = { toggleCalendar() } )
                .setToggled(toggled = calendarEnabled)
    }

    private fun initLessonsList() {
        val lessons = lessonsService
                .getAllLessons(weekIndex)
                .filter {
                    !filterEnabled || !lessonStateService.isLessonFilled(it.lesson, weekIndex)
                }

        monitoringLessonsListView.fill(
                LessonsViewData(
                        weekIndex = weekIndex,
                        lessonsData = lessons.map { groupAndLesson ->
                            LessonRowViewData(
                                    staffMember = staffMembersStorageService.getStaffMember(groupAndLesson.lesson.teacherLogin),
                                    group = groupAndLesson.group,
                                    lesson = groupAndLesson.lesson,
                                    lessonStatus = lessonStatusService.getLessonStatus(
                                            lessonId = groupAndLesson.lesson.id,
                                            lessonTime = lessonsService.getLessonStartTime(groupAndLesson.lesson.id, weekIndex)
                                    ),
                                    isLessonFinished = lessonStateService.isLessonFinished(
                                            lessonId = groupAndLesson.lesson.id,
                                            weekIndex = weekIndex
                                    ),
                                    isLessonAttendanceFilled = lessonsAttendancesService.isLessonAttendanceFilled(
                                            lessonId = groupAndLesson.lesson.id,
                                            weekIndex = weekIndex
                                    ),
                                    studentsToAttendanceType = lessonsService
                                            .getLessonStudents(groupAndLesson.lesson.id, weekIndex)
                                            .map { student ->
                                                Pair(
                                                        student,
                                                        studentsAttendancesProviderService.getAttendance(
                                                                groupAndLesson.lesson.id,
                                                                student.login,
                                                                weekIndex
                                                        )
                                                )
                                            },
                                    weekIndex = weekIndex,
                                    showTeacher = true
                            )
                        }
                )
        )

        monitoringLessonsNoLessonsView.visibility = visibleElseGone(visible = (lessons.isEmpty() && filterEnabled))
    }

    private fun toggleFilter() {
        filterEnabled = !filterEnabled

        monitoringLessonsHeaderView.getFirstButtonHandler().setToggled(toggled = filterEnabled)

        initLessonsList()
    }

    private fun toggleCalendar() {
        calendarEnabled = !calendarEnabled

        monitoringLessonsHeaderView.getSecondButtonHandler().setToggled(toggled = calendarEnabled)

        monitoringLessonsWeekSelectionBarView.visibility = visibleElseGone(visible = calendarEnabled)
    }

    override fun getAppLayoutView(): AppLayoutView? {
        return null
    }
}
