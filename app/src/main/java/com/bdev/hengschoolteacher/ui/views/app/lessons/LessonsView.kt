package com.bdev.hengschoolteacher.ui.views.app.lessons

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.data.school.DayOfWeek
import com.bdev.hengschoolteacher.data.school.group.GroupAndLesson
import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragment
import com.bdev.hengschoolteacher.ui.page_fragments.lesson.info.LessonInfoActivityData
import com.bdev.hengschoolteacher.ui.adapters.BaseWeekItemsListAdapter
import com.bdev.hengschoolteacher.ui.utils.ViewVisibilityUtils.visibleElseGone
import com.bdev.hengschoolteacher.ui.views.app.lesson.LessonRowViewData
import kotlinx.android.synthetic.main.view_lesson_item.view.*
import kotlinx.android.synthetic.main.view_lessons.view.*
import java.util.*

class LessonItemView : RelativeLayout {
    companion object {
        const val REQUEST_CODE_LESSON = 1
    }

    init {
        View.inflate(context, R.layout.view_lesson_item, this)
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    fun bind(data: LessonRowViewData): LessonItemView {
        lessonItemRowView.bind(data = data)

        lessonItemTeacherView.text = data.staffMember?.person?.name ?: "?"
        lessonItemTeacherView.visibility = visibleElseGone(visible = data.showTeacher)

        return this
    }
}

class LessonsListAdapter(context: Context) : BaseWeekItemsListAdapter<LessonRowViewData>(context) {
    private var weekIndex = 0

    fun setWeekIndex(weekIndex: Int) {
        this.weekIndex = weekIndex
    }

    override fun getElementView(item: LessonRowViewData, convertView: View?): View {
        return if (convertView == null || convertView !is LessonItemView) {
            LessonItemView(context)
        } else {
            convertView
        }.bind(data = item)
    }

    override fun getElementDayOfWeek(item: LessonRowViewData): DayOfWeek {
        return item.lesson.day
    }

    override fun getElementComparator(): Comparator<LessonRowViewData> {
        return Comparator { o1, o2 ->
            GroupAndLesson.getComparator(Calendar.getInstance()).compare(
                    GroupAndLesson(group = o1.group, lesson = o1.lesson),
                    GroupAndLesson(group = o2.group, lesson = o2.lesson)
            )
        }
    }
}

data class LessonsViewData(
        val weekIndex: Int,
        val lessonsData: List<LessonRowViewData>
)

class LessonsView : RelativeLayout {
    init {
        View.inflate(context, R.layout.view_lessons, this)
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    private lateinit var adapter: LessonsListAdapter

    private var weekIndex = 0

    fun bind() {
        adapter = LessonsListAdapter(context = context)

        lessonsListView.adapter = adapter

        lessonsListView.setOnItemClickListener { _, _, position, _ ->
            adapter.getItem(position).second?.let { data ->
//                LessonInfoActivityLauncher.launchAsChild(
//                        from = context as BasePageFragment,
//                        data = LessonInfoActivityData(
//                                groupId = data.group.id,
//                                lessonId = data.lesson.id,
//                                weekIndex = weekIndex
//                        ),
//                        requestCode = LessonItemView.REQUEST_CODE_LESSON
//                )
            }
        }
    }

    fun fill(data: LessonsViewData) {
        this.weekIndex = data.weekIndex

        adapter.setItems(data.lessonsData)
        adapter.setWeekIndex(weekIndex)
        adapter.notifyDataSetChanged()
    }
}
