package com.bdev.hengschoolteacher.ui.activities.profile

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.data.school.staff.StaffMember
import com.bdev.hengschoolteacher.interactors.LessonStateServiceImpl
import com.bdev.hengschoolteacher.interactors.lessons_status.LessonStatusStorageInteractorImpl
import com.bdev.hengschoolteacher.interactors.LessonsAttendancesServiceImpl
import com.bdev.hengschoolteacher.interactors.alerts.profile.AlertsProfileInteractorImpl
import com.bdev.hengschoolteacher.interactors.lessons.LessonsInteractorImpl
import com.bdev.hengschoolteacher.interactors.students.StudentsStorageInteractor
import com.bdev.hengschoolteacher.interactors.profile.ProfileServiceImpl
import com.bdev.hengschoolteacher.interactors.staff.StaffMembersStorageServiceImpl
import com.bdev.hengschoolteacher.interactors.students.StudentsStorageInteractorImpl
import com.bdev.hengschoolteacher.interactors.students_attendances.StudentsAttendancesProviderServiceImpl
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import com.bdev.hengschoolteacher.ui.utils.RedirectBuilder
import com.bdev.hengschoolteacher.ui.utils.ViewVisibilityUtils.visibleElseGone
import com.bdev.hengschoolteacher.ui.views.app.AppLayoutView
import com.bdev.hengschoolteacher.ui.views.app.AppMenuView
import com.bdev.hengschoolteacher.ui.views.app.lesson.LessonRowViewData
import com.bdev.hengschoolteacher.ui.views.app.lessons.LessonItemView
import com.bdev.hengschoolteacher.ui.views.app.lessons.LessonsViewData
import com.bdev.hengschoolteacher.ui.views.app.profile.ProfileHeaderView
import kotlinx.android.synthetic.main.activity_profile_lessons.*
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EActivity

@SuppressLint("Registered")
@EActivity(R.layout.activity_profile_lessons)
open class ProfileLessonsActivity : BaseActivity() {
    companion object {
        fun redirectToSibling(current: BaseActivity) {
            RedirectBuilder
                    .redirect(current)
                    .to(ProfileLessonsActivity_::class.java)
                    .goAndCloseCurrent()
        }
    }

    @Bean
    lateinit var lessonsService: LessonsInteractorImpl
    @Bean(StudentsStorageInteractorImpl::class)
    lateinit var studentsStorageInteractor: StudentsStorageInteractor
    @Bean
    lateinit var lessonStatusService: LessonStatusStorageInteractorImpl
    @Bean
    lateinit var lessonStateService: LessonStateServiceImpl
    @Bean
    lateinit var profileService: ProfileServiceImpl
    @Bean
    lateinit var alertsProfileService: AlertsProfileInteractorImpl
    @Bean
    lateinit var staffMembersStorageService: StaffMembersStorageServiceImpl
    @Bean
    lateinit var studentsAttendancesProviderService: StudentsAttendancesProviderServiceImpl
    @Bean
    lateinit var lessonsAttendancesService: LessonsAttendancesServiceImpl

    private var me: StaffMember? = null

    private var filterEnabled = true
    private var calendarEnabled = false

    private var weekIndex = 0

    @AfterViews
    fun init() {
        me = profileService.getMe()

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
