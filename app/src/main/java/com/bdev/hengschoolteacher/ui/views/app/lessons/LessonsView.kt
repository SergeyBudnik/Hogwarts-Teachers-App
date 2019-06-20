package com.bdev.hengschoolteacher.ui.views.app.lessons

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.data.school.DayOfWeek
import com.bdev.hengschoolteacher.data.school.group.Group
import com.bdev.hengschoolteacher.data.school.group.GroupAndLesson
import com.bdev.hengschoolteacher.data.school.group.Lesson
import com.bdev.hengschoolteacher.service.LessonsService
import com.bdev.hengschoolteacher.service.StudentsAttendancesService
import com.bdev.hengschoolteacher.service.StudentsService
import com.bdev.hengschoolteacher.service.teacher.TeacherStorageService
import com.bdev.hengschoolteacher.ui.adapters.BaseWeekItemsListAdapter
import kotlinx.android.synthetic.main.view_lesson_item.view.*
import kotlinx.android.synthetic.main.view_lessons.view.*
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EViewGroup
import java.util.*
import kotlin.Comparator

@EViewGroup(R.layout.view_lesson_item)
open class LessonItemView : RelativeLayout {
    @Bean
    lateinit var studentsAttendancesService: StudentsAttendancesService
    @Bean
    lateinit var lessonsService: LessonsService
    @Bean
    lateinit var teacherStorageService: TeacherStorageService
    @Bean
    lateinit var studentsService: StudentsService

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    fun bind(
            group: Group,
            lesson: Lesson,
            weekIndex: Int,
            showTeacher: Boolean,
            clickAction: (Group, Lesson, Int) -> Unit
    ): LessonItemView {
        val students = lessonsService.getLessonStudents(lesson.id, weekIndex)

        lessonItemRowView.bind(group, lesson, students, weekIndex)

        setOnClickListener {
            clickAction.invoke(group, lesson, weekIndex)
        }

        lessonItemTeacherView.text = teacherStorageService.getTeacherById(lesson.teacherId)?.name ?: ""
        lessonItemTeacherView.visibility = if (showTeacher) { View.VISIBLE } else { View.GONE }

        return this
    }
}

class LessonsListAdapter(
        context: Context,
        private val showTeacher: Boolean,
        private val clickAction: (Group, Lesson, Int) -> Unit
) : BaseWeekItemsListAdapter<GroupAndLesson>(context) {
    private var weekIndex = 0

    fun setWeekIndex(weekIndex: Int) {
        this.weekIndex = weekIndex
    }

    override fun getElementView(item: GroupAndLesson, convertView: View?): View {
        return if (convertView == null || convertView !is LessonItemView) {
            LessonItemView_.build(context)
        } else {
            convertView
        }.bind(
                group = item.group,
                lesson = item.lesson,
                weekIndex = weekIndex,
                showTeacher = showTeacher,
                clickAction = clickAction
        )
    }

    override fun getElementDayOfWeek(item: GroupAndLesson): DayOfWeek {
        return item.lesson.day
    }

    override fun getElementComparator(): Comparator<GroupAndLesson> {
        return GroupAndLesson.getComparator(Calendar.getInstance())
    }
}

@EViewGroup(R.layout.view_lessons)
open class LessonsView : RelativeLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    private lateinit var adapter: LessonsListAdapter

    fun bind(showTeacher: Boolean, clickAction: (Group, Lesson, Int) -> Unit) {
        adapter = LessonsListAdapter(
                context = context,
                showTeacher = showTeacher,
                clickAction = clickAction
        )

        lessonsListView.adapter = adapter
    }

    fun setLessons(lessons: List<GroupAndLesson>) {
        adapter.setItems(lessons)
        adapter.notifyDataSetChanged()
    }

    fun setWeekIndex(weekIndex: Int) {
        adapter.setWeekIndex(weekIndex)
        adapter.notifyDataSetChanged()
    }
}