package com.bdev.hengschoolteacher.ui.activities.profile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.data.school.staff.StaffMember
import com.bdev.hengschoolteacher.interactors.LessonStateService
import com.bdev.hengschoolteacher.interactors.LessonsAttendancesService
import com.bdev.hengschoolteacher.interactors.alerts.profile.AlertsProfileInteractor
import com.bdev.hengschoolteacher.interactors.lessons.LessonsInteractor
import com.bdev.hengschoolteacher.interactors.lessons_status.LessonsStatusStorageInteractor
import com.bdev.hengschoolteacher.interactors.profile.ProfileInteractor
import com.bdev.hengschoolteacher.interactors.staff_members.StaffMembersStorageInteractor
import com.bdev.hengschoolteacher.interactors.students.StudentsStorageInteractor
import com.bdev.hengschoolteacher.interactors.students_attendances.StudentsAttendancesProviderInteractor
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import com.bdev.hengschoolteacher.ui.utils.RedirectBuilder
import com.bdev.hengschoolteacher.ui.utils.ViewVisibilityUtils.visibleElseGone
import com.bdev.hengschoolteacher.ui.views.app.AppLayoutView
import com.bdev.hengschoolteacher.ui.views.app.AppMenuView
import com.bdev.hengschoolteacher.ui.views.app.lesson.LessonRowViewData
import com.bdev.hengschoolteacher.ui.views.app.lessons.LessonItemView
import com.bdev.hengschoolteacher.ui.views.app.lessons.LessonsViewData
import com.bdev.hengschoolteacher.ui.views.app.profile.ProfileHeaderView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_profile_lessons.*
import javax.inject.Inject

@AndroidEntryPoint
class ProfileLessonsActivity : BaseActivity() {
    companion object {
        fun redirectToSibling(current: BaseActivity) {
            RedirectBuilder
                    .redirect(current)
                    .to(ProfileLessonsActivity::class.java)
                    .goAndCloseCurrent()
        }
    }

    @Inject lateinit var lessonsService: LessonsInteractor
    @Inject lateinit var studentsStorageInteractor: StudentsStorageInteractor
    @Inject lateinit var lessonsStatusService: LessonsStatusStorageInteractor
    @Inject lateinit var lessonStateService: LessonStateService
    @Inject lateinit var profileInteractor: ProfileInteractor
    @Inject lateinit var alertsProfileService: AlertsProfileInteractor
    @Inject lateinit var staffMembersStorageInteractor: StaffMembersStorageInteractor
    @Inject lateinit var studentsAttendancesProviderInteractor: StudentsAttendancesProviderInteractor
    @Inject lateinit var lessonsAttendancesService: LessonsAttendancesService

    private var me: StaffMember? = null

    private var filterEnabled = true
    private var calendarEnabled = false

    private var weekIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_profile_lessons)

        me = profileInteractor.getMe()

        initHeader()

        profileLessonsSecondaryHeaderView.bind(
                ProfileHeaderView.Item.LESSONS,
                hasLessonsAlert = alertsProfileService.haveLessonsAlerts(),
                hasDebtsAlert = alertsProfileService.haveDebtsAlerts(),
                hasPaymentsAlert = alertsProfileService.havePaymentsAlerts()
        )

        profileLessonsMenuLayoutView.setCurrentMenuItem(AppMenuView.Item.MY_PROFILE)

        profileLessonsListView.bind()

        profileLessonsWeekSelectionBarView.init { weekIndex ->
            this.weekIndex = weekIndex

            initLessonsList()
        }

        profileLessonsNoLessonsView.bind { toggleFilter() }
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
        profileLessonsHeaderView
                .setLeftButtonAction { profileLessonsMenuLayoutView.openMenu() }

        profileLessonsHeaderView.getFirstButtonHandler()
                .setAction(action = { toggleFilter() })
                .setToggled(toggled = filterEnabled)

        profileLessonsHeaderView.getSecondButtonHandler()
                .setAction(action = { toggleCalendar() })
                .setToggled(toggled = calendarEnabled)
    }

    private fun initLessonsList() {
        me?.let { me ->
            val lessons = lessonsService
                    .getTeacherLessons(teacherLogin = me.login, weekIndex = weekIndex)
                    .filter {
                        !filterEnabled || !lessonStateService.isLessonFilled(it.lesson, weekIndex)
                    }

            profileLessonsListView.fill(
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

            profileLessonsNoLessonsView.visibility = visibleElseGone(visible = (lessons.isEmpty() && filterEnabled))
        }
    }

    private fun toggleFilter() {
        filterEnabled = !filterEnabled

        profileLessonsHeaderView.getFirstButtonHandler().setToggled(toggled = filterEnabled)

        initLessonsList()
    }

    private fun toggleCalendar() {
        calendarEnabled = !calendarEnabled

        profileLessonsHeaderView.getSecondButtonHandler().setToggled(toggled = calendarEnabled)

        profileLessonsWeekSelectionBarView.visibility = visibleElseGone(visible = calendarEnabled)
    }

    override fun getAppLayoutView(): AppLayoutView? {
        return profileLessonsMenuLayoutView
    }
}
