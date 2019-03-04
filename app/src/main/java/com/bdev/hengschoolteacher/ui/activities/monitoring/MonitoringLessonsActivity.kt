package com.bdev.hengschoolteacher.ui.activities.monitoring

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
import com.bdev.hengschoolteacher.service.*
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import com.bdev.hengschoolteacher.ui.activities.lesson.LessonActivity
import com.bdev.hengschoolteacher.ui.activities.lesson.LessonActivity_
import com.bdev.hengschoolteacher.ui.adapters.BaseWeekItemsListAdapter
import com.bdev.hengschoolteacher.ui.utils.RedirectUtils
import com.bdev.hengschoolteacher.ui.views.app.AppMenuView
import kotlinx.android.synthetic.main.activity_monitoring_lessons.*
import kotlinx.android.synthetic.main.view_item_monitoring_lessons.view.*
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EActivity
import org.androidannotations.annotations.EViewGroup

@EViewGroup(R.layout.view_item_monitoring_lessons)
open class MonitoringLessonItemView : RelativeLayout {
    @Bean
    lateinit var studentsAttendancesService: StudentsAttendancesService
    @Bean
    lateinit var lessonsService: LessonsService
    @Bean
    lateinit var teachersService: TeachersService
    @Bean
    lateinit var studentsService: StudentsService

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    fun bind(group: Group, lesson: Lesson): MonitoringLessonItemView {
        val students = studentsService.getGroupStudents(group.id)

        allLessonRowView.bind(group, lesson, students)

        setOnClickListener {
            RedirectUtils.redirect(context as BaseActivity)
                    .to(LessonActivity_::class.java)
                    .withExtra(LessonActivity.EXTRA_GROUP_ID, group.id)
                    .withExtra(LessonActivity.EXTRA_LESSON_ID, lesson.id)
                    .withAnim(R.anim.slide_open_enter, R.anim.slide_open_exit)
                    .goForResult(MonitoringLessonsActivity.REQUEST_CODE_LESSON)
        }

        teacherView.text = (teachersService.getTeacherById(lesson.teacherId) ?: throw RuntimeException()).name

        return this
    }
}

open class MonitoringLessonsListAdapter(context: Context) : BaseWeekItemsListAdapter<GroupAndLesson>(context) {
    override fun getElementView(item: GroupAndLesson, convertView: View?): View {
        return if (convertView == null || convertView !is MonitoringLessonItemView) {
            MonitoringLessonItemView_.build(context)
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
@EActivity(R.layout.activity_monitoring_lessons)
open class MonitoringLessonsActivity : BaseActivity() {
    companion object {
        const val REQUEST_CODE_LESSON = 1
    }

    @Bean
    lateinit var lessonsService: LessonsService
    @Bean
    lateinit var studentsService: StudentsService
    @Bean
    lateinit var lessonStatusService: LessonStatusService
    @Bean
    lateinit var lessonsAttendancesService: LessonsAttendancesService

    private var filterEnabled = true

    @AfterViews
    fun init() {
        monitoringLessonsHeaderView
                .setLeftButtonAction { monitoringLessonsMenuLayoutView.openMenu() }
                .setFirstRightButtonAction { toggleFilter() }
                .setFirstRightButtonColor(getFilterColor())

        monitoringLessonsMenuLayoutView.setCurrentMenuItem(AppMenuView.Item.MONITORING)

        initLessonsList()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == MonitoringLessonsActivity.REQUEST_CODE_LESSON) {
            if (resultCode == Activity.RESULT_OK) {
                initLessonsList()
            }
        }
    }

    private fun initLessonsList() {
        val lessons = lessonsService.getAllLessons()
                .filter {
                    val attendanceFilled = lessonsAttendancesService.isLessonAttendanceFilled(it.group, it.lesson, 0)

                    !filterEnabled || !attendanceFilled
                }

        val adapter = MonitoringLessonsListAdapter(this)

        adapter.setItems(lessons)

        monitoringLessonsListView.adapter = adapter
    }

    private fun toggleFilter() {
        filterEnabled = !filterEnabled

        monitoringLessonsHeaderView.setFirstRightButtonColor(getFilterColor())

        initLessonsList()
    }

    private fun getFilterColor(): Int {
        return resources.getColor(if (filterEnabled) { R.color.fill_text_action_link } else { R.color.fill_text_base })
    }
}
