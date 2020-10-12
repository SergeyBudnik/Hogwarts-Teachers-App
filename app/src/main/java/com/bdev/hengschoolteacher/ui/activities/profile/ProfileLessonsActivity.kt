package com.bdev.hengschoolteacher.ui.activities.profile

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.data.school.staff.StaffMember
import com.bdev.hengschoolteacher.service.LessonStateService
import com.bdev.hengschoolteacher.service.LessonStatusService
import com.bdev.hengschoolteacher.service.LessonsService
import com.bdev.hengschoolteacher.service.StudentsService
import com.bdev.hengschoolteacher.service.profile.ProfileService
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import com.bdev.hengschoolteacher.ui.utils.RedirectBuilder
import com.bdev.hengschoolteacher.ui.utils.ViewVisibilityUtils.visibleElseGone
import com.bdev.hengschoolteacher.ui.views.app.AppLayoutView
import com.bdev.hengschoolteacher.ui.views.app.AppMenuView
import com.bdev.hengschoolteacher.ui.views.app.lessons.LessonItemView
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
    lateinit var lessonsService: LessonsService
    @Bean
    lateinit var studentsService: StudentsService
    @Bean
    lateinit var lessonStatusService: LessonStatusService
    @Bean
    lateinit var lessonStateService: LessonStateService
    @Bean
    lateinit var profileService: ProfileService

    private var me: StaffMember? = null

    private var filterEnabled = true
    private var calendarEnabled = false

    private var weekIndex = 0

    @AfterViews
    fun init() {
        me = profileService.getMe()

        profileLessonsHeaderView
                .setLeftButtonAction { profileLessonsMenuLayoutView.openMenu() }
                .setFirstRightButtonAction { toggleFilter() }
                .setFirstRightButtonActive(filterEnabled)
                .setSecondRightButtonAction { toggleCalendar() }
                .setSecondRightButtonActive(calendarEnabled)

        profileLessonsSecondaryHeaderView.bind(ProfileHeaderView.Item.LESSONS)

        profileLessonsMenuLayoutView.setCurrentMenuItem(AppMenuView.Item.MY_PROFILE)

        profileLessonsListView.bind(showTeacher = false)

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

    private fun initLessonsList() {
        me?.let { me ->
            val lessons = lessonsService
                    .getTeacherLessons(teacherLogin = me.login, weekIndex = weekIndex)
                    .filter {
                        !filterEnabled || !lessonStateService.isLessonFilled(it.lesson, weekIndex)
                    }

            profileLessonsListView.fill(lessons, weekIndex)

            profileLessonsNoLessonsView.visibility = visibleElseGone(visible = (lessons.isEmpty() && filterEnabled))
        }
    }

    private fun toggleFilter() {
        filterEnabled = !filterEnabled

        profileLessonsHeaderView.setFirstRightButtonActive(filterEnabled)

        initLessonsList()
    }

    private fun toggleCalendar() {
        calendarEnabled = !calendarEnabled

        profileLessonsHeaderView.setSecondRightButtonActive(calendarEnabled)

        profileLessonsWeekSelectionBarView.visibility = visibleElseGone(visible = calendarEnabled)
    }

    override fun getAppLayoutView(): AppLayoutView? {
        return profileLessonsMenuLayoutView
    }
}
