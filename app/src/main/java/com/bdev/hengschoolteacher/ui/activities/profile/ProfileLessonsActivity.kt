package com.bdev.hengschoolteacher.ui.activities.profile

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.widget.RelativeLayout
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.data.school.DayOfWeek
import com.bdev.hengschoolteacher.data.school.group.Group
import com.bdev.hengschoolteacher.data.school.group.GroupAndLesson
import com.bdev.hengschoolteacher.data.school.group.Lesson
import com.bdev.hengschoolteacher.data.school.teacher.Teacher
import com.bdev.hengschoolteacher.service.*
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import com.bdev.hengschoolteacher.ui.activities.lesson.LessonActivity
import com.bdev.hengschoolteacher.ui.activities.lesson.LessonActivity_
import com.bdev.hengschoolteacher.ui.utils.RedirectUtils.Companion.redirect
import com.bdev.hengschoolteacher.ui.views.app.AppMenuView
import kotlinx.android.synthetic.main.activity_profile_lessons.*
import kotlinx.android.synthetic.main.view_item_profile_lessons.view.*
import kotlinx.android.synthetic.main.view_item_profile_lessons_day.view.*
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EActivity
import org.androidannotations.annotations.EViewGroup

@EViewGroup(R.layout.view_item_profile_lessons_day)
open class ProfileLessonsDayItemView : RelativeLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    fun bind(dayOfWeek: DayOfWeek): ProfileLessonsDayItemView {
        profileLessonsDayView.text = context.getString(dayOfWeek.shortNameId)

        return this
    }
}

@EViewGroup(R.layout.view_item_profile_lessons)
open class ProfileLessonsItemView : RelativeLayout {
    @Bean
    lateinit var studentsAttendancesService: StudentsAttendancesService
    @Bean
    lateinit var lessonsService: LessonsService
    @Bean
    lateinit var studentsService: StudentsService

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    fun bind(group: Group, lesson: Lesson): ProfileLessonsItemView {
        profileLessonsItemView.bind(group, lesson, studentsService.getGroupStudents(group.id))

        setOnClickListener {
            redirect(context as BaseActivity)
                    .to(LessonActivity_::class.java)
                    .withExtra(LessonActivity.EXTRA_GROUP_ID, group.id)
                    .withExtra(LessonActivity.EXTRA_LESSON_ID, lesson.id)
                    .withAnim(R.anim.slide_open_enter, R.anim.slide_open_exit)
                    .goForResult(ProfileLessonsActivity.REQUEST_CODE_LESSON)
        }

        return this
    }
}

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
    lateinit var teachersService: TeachersService
    @Bean
    lateinit var userPreferencesService: UserPreferencesService

    private lateinit var me: Teacher

    @AfterViews
    fun init() {
        val login = userPreferencesService.getUserLogin() ?: throw RuntimeException()

        me = teachersService.getTeacherByLogin(login) ?: throw RuntimeException()

        profileLessonsHeaderView.setLeftButtonAction { profileLessonsMenuLayoutView.openMenu() }
        profileLessonsMenuLayoutView.setCurrentMenuItem(AppMenuView.Item.MY_PROFILE)

        initLessonsList(lessonsService.getTeacherLessons(me.id))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_LESSON) {
            if (resultCode == Activity.RESULT_OK) {
                initLessonsList(lessonsService.getTeacherLessons(me.id))
            }
        }
    }

    private fun initLessonsList(lessons: Map<DayOfWeek, List<GroupAndLesson>>) {
        profileLessonsContentView.removeAllViews()

        DayOfWeek.values().forEach { dayOfWeek ->
            val dayLessons = (lessons[dayOfWeek] ?: throw RuntimeException())

            if (dayLessons.isNotEmpty()) {
                profileLessonsContentView.addView(ProfileLessonsDayItemView_.build(this).bind(dayOfWeek))

                dayLessons.forEach { lessonAndGroup ->
                    val view = ProfileLessonsItemView_.build(this).bind(
                            lessonAndGroup.group, lessonAndGroup.lesson
                    )

                    profileLessonsContentView.addView(view)
                }
            }
        }
    }
}
