package com.bdev.hengschoolteacher.ui.activities.monitoring

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.RelativeLayout
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.data.school.DayOfWeek
import com.bdev.hengschoolteacher.data.school.group.Group
import com.bdev.hengschoolteacher.data.school.group.Lesson
import com.bdev.hengschoolteacher.service.*
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import com.bdev.hengschoolteacher.ui.activities.lesson.LessonActivity
import com.bdev.hengschoolteacher.ui.activities.lesson.LessonActivity_
import com.bdev.hengschoolteacher.ui.utils.RedirectUtils
import com.bdev.hengschoolteacher.ui.views.app.AppMenuView
import kotlinx.android.synthetic.main.activity_monitoring_lessons.*
import kotlinx.android.synthetic.main.view_item_monitoring_lessons.view.*
import kotlinx.android.synthetic.main.view_item_monitoring_lessons_day.view.*
import org.androidannotations.annotations.*

@EViewGroup(R.layout.view_item_monitoring_lessons_day)
open class MonitoringLessonDayItemView : RelativeLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    fun bind(dayOfWeek: DayOfWeek): MonitoringLessonDayItemView {
        dayView.text = context.getString(dayOfWeek.shortNameId)

        return this
    }
}

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

@EBean
open class MonitoringLessonsListAdapter : BaseAdapter() {
    enum class AllLessonsItemType(val index: Int) {
        DAY(0), LESSON(1)
    }

    @RootContext
    lateinit var context: Context

    private var items: List<Pair<DayOfWeek, Pair<Group, Lesson>?>> = emptyList()

    fun setLessons(lessons: Map<DayOfWeek, List<Pair<Group, Lesson>>>) {
        val items = ArrayList<Pair<DayOfWeek, Pair<Group, Lesson>?>>()

        for (day in DayOfWeek.values()) {
            val dayLessons = lessons[day] ?: emptyList()

            if (dayLessons.isNotEmpty()) {
                items.add(Pair(day, null))
                dayLessons.forEach { dayLesson -> items.add(Pair(day, dayLesson)) }
            }
        }

        this.items = items
    }

    override fun getViewTypeCount(): Int {
        return AllLessonsItemType.values().size
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).second == null) {
            AllLessonsItemType.DAY.index
        } else {
            AllLessonsItemType.LESSON.index
        }
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val item = getItem(position)

        val groupAndLesson = item.second

        return if (groupAndLesson == null) {
            if (convertView == null) {
                MonitoringLessonDayItemView_.build(context)
            } else {
                convertView as MonitoringLessonDayItemView
            }.bind(item.first)
        } else {
            if (convertView == null) {
                MonitoringLessonItemView_.build(context)
            } else {
                convertView as MonitoringLessonItemView
            }.bind(groupAndLesson.first, groupAndLesson.second)
        }
    }

    override fun getItem(position: Int): Pair<DayOfWeek, Pair<Group, Lesson>?> {
        return items[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return items.size
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
    lateinit var lessonsAttendancesService: LessonsAttendancesService
    @Bean
    lateinit var lessonStatusService: LessonStatusService

    @Bean
    lateinit var monitoringLessonsListAdapter: MonitoringLessonsListAdapter

    private var filterEnabled = true

    @AfterViews
    fun init() {
        monitoringLessonsHeaderView
                .setLeftButtonAction { monitoringLessonsMenuLayoutView.openMenu() }
                .setRightButtonAction { toggleFilter() }
                .setRightButtonColor(getFilterColor())

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

        val filteredLessons = HashMap<DayOfWeek, List<Pair<Group, Lesson>>>()

        for (day in lessons.keys) {
            val dayLessons = lessons[day] ?: emptyList()

            val filteredDayLessons = if (filterEnabled) {
                dayLessons
                        .asSequence()
                        .filter { lessonsService.isLessonFinished(it.second) }
                        .filter {
                            val attendanceFilled = lessonsAttendancesService.isLessonAttendanceFilled(it.first, it.second)
                            val statusFilled = lessonStatusService.getLessonStatus(it.second.id, lessonsService.getLessonStartTime(it.second.id)) != null

                            !attendanceFilled || !statusFilled
                        }
                        .toList()
            } else {
                dayLessons
            }

            filteredLessons[day] = filteredDayLessons
        }

        monitoringLessonsListAdapter.setLessons(filteredLessons)
        monitoringLessonsListView.adapter = monitoringLessonsListAdapter
    }

    private fun toggleFilter() {
        filterEnabled = !filterEnabled

        monitoringLessonsHeaderView.setRightButtonColor(getFilterColor())

        initLessonsList()
    }

    private fun getFilterColor(): Int {
        return resources.getColor(if (filterEnabled) { R.color.fill_text_action_link } else { R.color.fill_text_base })
    }
}
