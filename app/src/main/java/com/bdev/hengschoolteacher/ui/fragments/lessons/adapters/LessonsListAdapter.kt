package com.bdev.hengschoolteacher.ui.fragments.lessons.adapters

import android.content.Context
import android.view.View
import com.bdev.hengschoolteacher.data.school.DayOfWeek
import com.bdev.hengschoolteacher.data.school.group.GroupAndLesson
import com.bdev.hengschoolteacher.ui.adapters.BaseWeekItemsListAdapter
import com.bdev.hengschoolteacher.ui.fragments.lessons.LessonsFragmentItemData
import com.bdev.hengschoolteacher.ui.fragments.lessons.views.LessonsItemView
import com.bdev.hengschoolteacher.utils.CalendarUtils
import kotlin.Comparator

class LessonsListAdapter(context: Context) : BaseWeekItemsListAdapter<LessonsFragmentItemData>(context) {
    private var weekIndex = 0

    fun setWeekIndex(weekIndex: Int) {
        this.weekIndex = weekIndex
    }

    override fun getElementView(item: LessonsFragmentItemData, convertView: View?): View {
        return if (convertView == null || convertView !is LessonsItemView) {
            LessonsItemView(context)
        } else {
            convertView
        }.bind(data = item)
    }

    override fun getElementDayOfWeek(item: LessonsFragmentItemData): DayOfWeek {
        return item.lesson.day
    }

    override fun getElementComparator(): Comparator<LessonsFragmentItemData> {
        return Comparator { o1, o2 ->
            GroupAndLesson.getComparator().compare(
                    GroupAndLesson(group = o1.group, lesson = o1.lesson),
                    GroupAndLesson(group = o2.group, lesson = o2.lesson)
            )
        }
    }
}