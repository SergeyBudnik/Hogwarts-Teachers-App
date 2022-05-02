package com.bdev.hengschoolteacher.ui.fragments.lessons.adapters

import android.content.Context
import android.view.View
import com.bdev.hengschoolteacher.data.school.DayOfWeek
import com.bdev.hengschoolteacher.data.school.group.GroupAndLesson
import com.bdev.hengschoolteacher.ui.adapters.BaseWeekItemsListAdapter
import com.bdev.hengschoolteacher.ui.views.app.lesson.LessonRowViewData
import com.bdev.hengschoolteacher.ui.views.app.lessons.LessonItemView
import java.util.*
import kotlin.Comparator

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