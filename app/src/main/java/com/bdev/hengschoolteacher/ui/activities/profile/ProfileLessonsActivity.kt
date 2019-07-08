package com.bdev.hengschoolteacher.ui.activities.profile

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.view.View
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.data.school.teacher.Teacher
import com.bdev.hengschoolteacher.service.*
import com.bdev.hengschoolteacher.service.teacher.TeacherStorageService
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import com.bdev.hengschoolteacher.ui.utils.RedirectBuilder
import com.bdev.hengschoolteacher.ui.views.app.AppMenuView
import com.bdev.hengschoolteacher.ui.views.app.lessons.LessonItemView
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
    lateinit var teacherStorageService: TeacherStorageService
    @Bean
    lateinit var userPreferencesService: UserPreferencesService
    @Bean
    lateinit var lessonStatusService: LessonStatusService
    @Bean
    lateinit var lessonStateService: LessonStateService

    private lateinit var me: Teacher

    private var filterEnabled = true
    private var calendarEnabled = false

    private var weekIndex = 0

    @AfterViews
    fun init() {
        val login = userPreferencesService.getUserLogin() ?: throw RuntimeException()

        me = teacherStorageService.getTeacherByLogin(login) ?: throw RuntimeException()

        profileLessonsHeaderView
                .setLeftButtonAction { profileLessonsMenuLayoutView.openMenu() }
                .setFirstRightButtonAction { toggleFilter() }
                .setFirstRightButtonActive(filterEnabled)
                .setSecondRightButtonAction { toggleCalendar() }
                .setSecondRightButtonActive(calendarEnabled)

        profileLessonsMenuLayoutView.setCurrentMenuItem(AppMenuView.Item.MY_PROFILE)

        profileLessonsListView.bind(showTeacher = false)

        profileLessonsWeekSelectionBarView.init { weekIndex ->
            this.weekIndex = weekIndex

            initLessonsList()
        }

        profileLessonsNoLessonsView.bind { toggleFilter() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == LessonItemView.REQUEST_CODE_LESSON) {
            if (resultCode == Activity.RESULT_OK) {
                initLessonsList()
            }
        }
    }

    private fun initLessonsList() {
        val lessons = lessonsService
                .getTeacherLessons(me.id)
                .filter {
                    !filterEnabled || !lessonStateService.isLessonFilled(it.lesson, weekIndex)
                }

        profileLessonsListView.fill(lessons, weekIndex)

        profileLessonsNoLessonsView.visibility = if (lessons.isEmpty() && filterEnabled) {
            View.VISIBLE
        } else {
            View.GONE
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

        profileLessonsWeekSelectionBarView.visibility = if (calendarEnabled) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }
}
