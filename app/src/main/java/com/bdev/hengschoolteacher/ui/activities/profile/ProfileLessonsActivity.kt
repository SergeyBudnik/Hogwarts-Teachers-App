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
import com.bdev.hengschoolteacher.ui.activities.lesson.LessonActivity
import com.bdev.hengschoolteacher.ui.utils.HeaderElementsUtils
import com.bdev.hengschoolteacher.ui.views.app.AppMenuView
import kotlinx.android.synthetic.main.activity_profile_lessons.*
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EActivity

@SuppressLint("Registered")
@EActivity(R.layout.activity_profile_lessons)
open class ProfileLessonsActivity : BaseActivity() {
    companion object {
        const val REQUEST_CODE_LESSON = 1
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
    lateinit var lessonsAttendancesService: LessonsAttendancesService
    @Bean
    lateinit var lessonStatusService: LessonStatusService

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
                .setFirstRightButtonColor(getHeaderButtonColor(filterEnabled))
                .setSecondRightButtonAction { toggleCalendar() }
                .setSecondRightButtonColor(getHeaderButtonColor(calendarEnabled))

        profileLessonsMenuLayoutView.setCurrentMenuItem(AppMenuView.Item.MY_PROFILE)

        profileLessonsListView.bind(
                showTeacher = false,
                clickAction = {
                    group, lesson, weekIndex -> LessonActivity
                        .redirect(
                                context = this,
                                groupId = group.id,
                                lessonId = lesson.id,
                                weekIndex = weekIndex
                        )
                        .withAnim(R.anim.slide_open_enter, R.anim.slide_open_exit)
                        .goForResult(REQUEST_CODE_LESSON)
                }
        )

        profileLessonsWeekSelectionBarView.init { weekIndex ->
            this.weekIndex = weekIndex

            initLessonsList()
        }

        profileLessonsNoLessonsView.bind { toggleFilter() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_LESSON) {
            if (resultCode == Activity.RESULT_OK) {
                initLessonsList()
            }
        }
    }

    private fun initLessonsList() {
        val lessons = lessonsService.getTeacherLessons(me.id).filter {
            val attendanceFilled = lessonsAttendancesService.isLessonAttendanceFilled(
                    lessonId = it.lesson.id,
                    weekIndex = weekIndex
            )

            val statusFilled = lessonStatusService.getLessonStatus(
                    lessonId = it.lesson.id,
                    lessonTime = lessonsService.getLessonStartTime(it.lesson.id, weekIndex)
            ) != null

            !filterEnabled || !attendanceFilled || !statusFilled
        }

        profileLessonsListView.setLessons(lessons)
        profileLessonsListView.setWeekIndex(weekIndex)

        profileLessonsNoLessonsView.visibility = if (lessons.isEmpty() && filterEnabled) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    private fun toggleFilter() {
        filterEnabled = !filterEnabled

        profileLessonsHeaderView.setFirstRightButtonColor(getHeaderButtonColor(filterEnabled))

        initLessonsList()
    }

    private fun toggleCalendar() {
        calendarEnabled = !calendarEnabled

        profileLessonsHeaderView.setSecondRightButtonColor(getHeaderButtonColor(calendarEnabled))

        profileLessonsWeekSelectionBarView.visibility = if (calendarEnabled) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    private fun getHeaderButtonColor(enabled: Boolean): Int {
        return HeaderElementsUtils.getColor(this, enabled)
    }
}
