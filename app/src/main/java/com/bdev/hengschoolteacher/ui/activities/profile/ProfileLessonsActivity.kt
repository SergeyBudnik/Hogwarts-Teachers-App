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
import com.bdev.hengschoolteacher.ui.activities.lesson.LessonActivity_
import com.bdev.hengschoolteacher.ui.adapters.BaseWeekItemsListAdapter
import com.bdev.hengschoolteacher.ui.utils.RedirectUtils.Companion.redirect
import com.bdev.hengschoolteacher.ui.views.app.AppMenuView
import kotlinx.android.synthetic.main.activity_profile_lessons.*
import kotlinx.android.synthetic.main.view_item_profile_lessons.view.*
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EActivity
import org.androidannotations.annotations.EViewGroup
import com.bdev.hengschoolteacher.ui.utils.ListUtils

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

class ProfileLessonsListAdapter(context: Context): BaseWeekItemsListAdapter<GroupAndLesson>(context) {
    override fun getElementView(item: GroupAndLesson, convertView: View?): View {
        return if (convertView == null || convertView !is ProfileLessonsItemView) {
            ProfileLessonsItemView_.build(context)
        } else {
            convertView
        }.bind(item.group, item.lesson)
    }

    override fun getElementDayOfWeek(item: GroupAndLesson): DayOfWeek {
        return item.lesson.day
    }

    override fun getElementComparator(): Comparator<GroupAndLesson> {
        return GroupAndLesson.getComparator()
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

    private lateinit var me: Teacher

    private var filterEnabled = true

    @AfterViews
    fun init() {
        val login = userPreferencesService.getUserLogin() ?: throw RuntimeException()

        me = teachersService.getTeacherByLogin(login) ?: throw RuntimeException()

        profileLessonsHeaderView
                .setLeftButtonAction { profileLessonsMenuLayoutView.openMenu() }
                .setFirstRightButtonAction { toggleFilter() }
                .setFirstRightButtonColor(getFilterColor())

        profileLessonsMenuLayoutView.setCurrentMenuItem(AppMenuView.Item.MY_PROFILE)

        profileLessonsWeekSelectionBarView.setOnWeekChangedListener { startTime, finishTime ->
            initLessonsList()
        }
        profileLessonsWeekSelectionBarView.init()
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

        adapter.setItems(lessonsService.getTeacherLessons(me.id).filter {
            !filterEnabled || !lessonsAttendancesService.isLessonAttendanceFilled(it.group, it.lesson, 0)
        })

        profileLessonsListView.adapter = adapter
        profileLessonsListView.setSelectionFromTop(listState.first, listState.second)
    }

    private fun toggleFilter() {
        filterEnabled = !filterEnabled

        profileLessonsHeaderView.setFirstRightButtonColor(getFilterColor())

        initLessonsList()
    }

    private fun getFilterColor(): Int {
        return resources.getColor(if (filterEnabled) { R.color.fill_text_action_link } else { R.color.fill_text_base })
    }
}
