package com.bdev.hengschoolteacher.ui.adapters

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.bdev.hengschoolteacher.data.school.DayOfWeek
import com.bdev.hengschoolteacher.ui.views.app.common.ListDayItemView
import java.util.ArrayList

abstract class BaseWeekItemsListAdapter<T>(
    protected val context: Context
) : BaseAdapter() {
    private var sortedItems: List<Pair<DayOfWeek, T?>> = ArrayList()

    fun setItems(items: List<T>) {
        val rawSortedItems = items.sortedWith(getElementComparator())

        val result = ArrayList<Pair<DayOfWeek, T?>>()

        var currentDay: DayOfWeek? = null

        for (item in rawSortedItems) {
            val itemDay = getElementDayOfWeek(item)

            if (currentDay != itemDay) {
                result.add(Pair(itemDay, null))

                currentDay = itemDay
            }

            result.add(Pair(itemDay, item))
        }

        sortedItems = result
    }

    override fun getView(position: Int, convertView: View?, parentView: ViewGroup?): View {
        val item = getItem(position)

        val day = item.first
        val element = item.second

        return if (element == null) {
            val view = if (convertView == null || convertView !is ListDayItemView) {
                ListDayItemView(context)
            } else {
                convertView
            }.bind(day)

            view.setOnClickListener(null)

            view
        } else {
            getElementView(element, convertView)
        }
    }

    override fun getItem(position: Int): Pair<DayOfWeek, T?> {
        return sortedItems[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return sortedItems.size
    }

    protected abstract fun getElementView(item: T, convertView: View?): View
    protected abstract fun getElementDayOfWeek(item: T): DayOfWeek
    protected abstract fun getElementComparator(): Comparator<T>
}
