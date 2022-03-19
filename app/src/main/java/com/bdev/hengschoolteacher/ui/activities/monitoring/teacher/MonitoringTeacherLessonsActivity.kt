package com.bdev.hengschoolteacher.ui.activities.monitoring.teacher

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.interactors.LessonStateServiceImpl
import com.bdev.hengschoolteacher.interactors.lessons_status.LessonStatusStorageInteractorImpl
import com.bdev.hengschoolteacher.interactors.LessonsAttendancesServiceImpl
import com.bdev.hengschoolteacher.interactors.alerts.monitoring.AlertsMonitoringTeachersInteractorImpl
import com.bdev.hengschoolteacher.interactors.lessons.LessonsInteractorImpl
import com.bdev.hengschoolteacher.interactors.staff.StaffMembersStorageServiceImpl
import com.bdev.hengschoolteacher.interactors.students_attendances.StudentsAttendancesProviderServiceImpl
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import com.bdev.hengschoolteacher.ui.activities.teacher.TeacherActivity
import com.bdev.hengschoolteacher.ui.utils.RedirectBuilder
import com.bdev.hengschoolteacher.ui.utils.ViewVisibilityUtils.visibleElseGone
import com.bdev.hengschoolteacher.ui.views.app.AppLayoutView
import com.bdev.hengschoolteacher.ui.views.app.lesson.LessonRowViewData
import com.bdev.hengschoolteacher.ui.views.app.lessons.LessonItemView
import com.bdev.hengschoolteacher.ui.views.app.lessons.LessonsViewData
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
    lateinit var lessonsService: LessonsInteractorImpl
    @Bean
    lateinit var lessonStateService: LessonStateServiceImpl
    @Bean
    lateinit var alertsMonitoringTeachersService: AlertsMonitoringTeachersInteractorImpl
    @Bean
    lateinit var staffMembersStorageService: StaffMembersStorageServiceImpl
    @Bean
    lateinit var studentsAttendancesProviderService: StudentsAttendancesProviderServiceImpl
    @Bean
    lateinit var lessonsAttendancesService: LessonsAttendancesServiceImpl
    @Bean
    lateinit var lessonStatusService: LessonStatusStorageInteractorImpl

    private var weekIndex = 0

    private var filterEnabled = true
    private var calendarEnabled = false

    @AfterViews
    fun init() {
        initHeader()

        monitoringTeacherLessonsSecondaryHeaderView.bind(
                currentItem = MonitoringTeacherHeaderView.Item.LESSONS,
                teacherLogin = teacherLogin,
                hasLessonsAlert = alertsMonitoringTeachersService.haveLessonsAlerts(teacherLogin = teacherLogin),
                hasPaymentsAlert = alertsMonitoringTeachersService.havePaymentsAlerts(teacherLogin = teacherLogin),
                hasDebtsAlert = alertsMonitoringTeachersService.haveDebtsAlerts(teacherLogin = teacherLogin)
        )

        monitoringTeacherLessonsListView.bind()

        monitoringTeacherLessonsWeekSelectionBarView.init { weekIndex ->
            this.weekIndex = weekIndex

            initLessonsList()
        }
    }

    override fun onBackPressed() {
        doFinish()
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
        monitoringTeacherLessonsHeaderView
                .setLeftButtonAction { doFinish() }

        monitoringTeacherLessonsHeaderView.getFirstButtonHandler()
                .setAction(action = { toggleFilter() })
                .setToggled(toggled = filterEnabled)

        monitoringTeacherLessonsHeaderView.getSecondButtonHandler()
                .setAction(action = { toggleCalendar() })
                .setToggled(toggled = calendarEnabled)

        monitoringTeacherLessonsHeaderView.getThirdButtonHandler()
                .setAction(action = {
                    TeacherActivity.redirectToChild(
                            current = this,
                            teacherLogin = teacherLogin
                    )
                })
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

        monitoringTeacherLessonsListView.fill(
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
                                    showTeacher = false
                            )
                        }
                )
        )
    }

    private fun toggleFilter() {
        filterEnabled = !filterEnabled

        monitoringTeacherLessonsHeaderView.getFirstButtonHandler().setToggled(toggled = filterEnabled)

        initLessonsList()
    }

    private fun toggleCalendar() {
        calendarEnabled = !calendarEnabled

        monitoringTeacherLessonsHeaderView.getSecondButtonHandler().setToggled(toggled = calendarEnabled)

        monitoringTeacherLessonsWeekSelectionBarView.visibility = visibleElseGone(visible = calendarEnabled)
    }

    override fun getAppLayoutView(): AppLayoutView? {
        return null
    }
}
