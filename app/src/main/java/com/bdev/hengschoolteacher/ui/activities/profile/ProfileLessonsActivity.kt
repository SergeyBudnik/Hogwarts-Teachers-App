package com.bdev.hengschoolteacher.ui.activities.profile

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.view.View
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
import com.bdev.hengschoolteacher.ui.adapters.BaseWeekItemsListAdapter
import com.bdev.hengschoolteacher.ui.utils.ListUtils
import com.bdev.hengschoolteacher.ui.views.app.AppMenuView
import kotlinx.android.synthetic.main.activity_profile_lessons.*
import kotlinx.android.synthetic.main.view_item_profile_lessons.view.*
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EActivity
import org.androidannotations.annotations.EViewGroup
import java.util.*

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

    fun bind(group: Group, lesson: Lesson, weekIndex: Int): ProfileLessonsItemView {
        profileLessonsItemView.bind(
                group,
                lesson,
                lessonsService.getLessonStudents(
                        lessonId = lesson.id,
                        weekIndex = weekIndex
                ),
                weekIndex
        )

        setOnClickListener {
            LessonActivity
                    .redirectWithExtras(
                            context = context,
                            groupId = group.id,
                            lessonId = lesson.id,
                            weekIndex = weekIndex
                    )
                    .withAnim(R.anim.slide_open_enter, R.anim.slide_open_exit)
                    .goForResult(ProfileLessonsActivity.REQUEST_CODE_LESSON)
        }

        return this
    }
}

class ProfileLessonsListAdapter(context: Context): BaseWeekItemsListAdapter<GroupAndLesson>(context) {
    private var weekIndex = 0

    fun setWeekIndex(weekIndex: Int) {
        this.weekIndex = weekIndex
    }

    override fun getElementView(item: GroupAndLesson, convertView: View?): View {
        return if (convertView == null || convertView !is ProfileLessonsItemView) {
            ProfileLessonsItemView_.build(context)
        } else {
            convertView
        }.bind(item.group, item.lesson, weekIndex)
    }

    override fun getElementDayOfWeek(item: GroupAndLesson): DayOfWeek {
        return item.lesson.day
    }

    override fun getElementComparator(): Comparator<GroupAndLesson> {
        return GroupAndLesson.getComparator(Calendar.getInstance())
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

        me = teachersService.getTeacherByLogin(login) ?: throw RuntimeException()

        profileLessonsHeaderView
                .setLeftButtonAction { profileLessonsMenuLayoutView.openMenu() }
                .setFirstRightButtonAction { toggleFilter() }
                .setFirstRightButtonColor(getHeaderButtonColor(filterEnabled))
                .setSecondRightButtonAction { toggleCalendar() }
                .setSecondRightButtonColor(getHeaderButtonColor(calendarEnabled))

        profileLessonsMenuLayoutView.setCurrentMenuItem(AppMenuView.Item.MY_PROFILE)

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
        val listState = ListUtils.getListScrollState(profileLessonsListView)

        val adapter = ProfileLessonsListAdapter(this)

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

        adapter.setWeekIndex(weekIndex)
        adapter.setItems(lessons)

        profileLessonsListView.adapter = adapter
        profileLessonsListView.setSelectionFromTop(listState.first, listState.second)

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
        return resources.getColor(if (enabled) { R.color.fill_text_basic_action_link } else { R.color.fill_text_basic })
    }
}
