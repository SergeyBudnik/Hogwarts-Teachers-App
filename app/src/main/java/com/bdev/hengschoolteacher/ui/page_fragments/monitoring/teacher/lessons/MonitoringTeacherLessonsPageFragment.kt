package com.bdev.hengschoolteacher.ui.page_fragments.monitoring.teacher.lessons

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.interactors.LessonStateService
import com.bdev.hengschoolteacher.interactors.LessonsAttendancesService
import com.bdev.hengschoolteacher.interactors.alerts.monitoring.AlertsMonitoringTeachersInteractor
import com.bdev.hengschoolteacher.interactors.lessons.LessonsInteractor
import com.bdev.hengschoolteacher.interactors.lessons_status.LessonsStatusStorageInteractor
import com.bdev.hengschoolteacher.interactors.staff_members.StaffMembersStorageInteractor
import com.bdev.hengschoolteacher.interactors.students_attendances.StudentsAttendancesProviderInteractor
import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragment
import com.bdev.hengschoolteacher.ui.utils.ViewVisibilityUtils.visibleElseGone
import com.bdev.hengschoolteacher.ui.views.app.AppLayoutView
import com.bdev.hengschoolteacher.ui.views.app.lesson.LessonRowViewData
import com.bdev.hengschoolteacher.ui.views.app.lessons.LessonItemView
import com.bdev.hengschoolteacher.ui.views.app.lessons.LessonsViewData
import com.bdev.hengschoolteacher.ui.views.app.monitoring.teacher.MonitoringTeacherHeaderView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_monitoring_teacher_lessons.*
import javax.inject.Inject

@AndroidEntryPoint
class MonitoringTeacherLessonsPageFragment : BasePageFragment<MonitoringTeacherLessonsPageFragmentViewModel>() {
    companion object {
        const val EXTRA_TEACHER_LOGIN = "EXTRA_TEACHER_LOGIN"
    }

    lateinit var teacherLogin: String

    @Inject lateinit var lessonsService: LessonsInteractor
    @Inject lateinit var lessonStateService: LessonStateService
    @Inject lateinit var alertsMonitoringTeachersService: AlertsMonitoringTeachersInteractor
    @Inject lateinit var staffMembersStorageInteractor: StaffMembersStorageInteractor
    @Inject lateinit var studentsAttendancesProviderInteractor: StudentsAttendancesProviderInteractor
    @Inject lateinit var lessonsAttendancesService: LessonsAttendancesService
    @Inject lateinit var lessonsStatusService: LessonsStatusStorageInteractor

    private var weekIndex = 0

    private var filterEnabled = true
    private var calendarEnabled = false

    override fun provideViewModel(): MonitoringTeacherLessonsPageFragmentViewModel =
        ViewModelProvider(this).get(MonitoringTeacherLessonsPageFragmentViewModelImpl::class.java)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.activity_monitoring_teacher_debts, container, false)

    override fun doOnViewCreated() {
        super.doOnViewCreated()

        // teacherLogin = intent.getStringExtra(EXTRA_TEACHER_LOGIN)!! todo

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
//                    TeacherPageFragment.redirectToChild(
//                            current = this,
//                            teacherLogin = teacherLogin
//                    )
                })
    }

    private fun doFinish() {
//        finish()
//        overridePendingTransition(R.anim.slide_close_enter, R.anim.slide_close_exit)
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
                                    staffMember = staffMembersStorageInteractor.getStaffMember(groupAndLesson.lesson.teacherLogin),
                                    group = groupAndLesson.group,
                                    lesson = groupAndLesson.lesson,
                                    lessonStatus = lessonsStatusService.getLessonStatus(
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
                                                        studentsAttendancesProviderInteractor.getAttendance(
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
