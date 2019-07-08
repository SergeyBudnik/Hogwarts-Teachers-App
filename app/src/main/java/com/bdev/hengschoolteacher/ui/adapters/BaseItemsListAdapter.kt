package com.bdev.hengschoolteacher.ui.adapters

import android.content.Context
import android.widget.BaseAdapter

abstract class BaseItemsListAdapter<T>(
        protected val context: Context
) : BaseAdapter() {
    private var items: List<T> = ArrayList()
    private var filter: (T) -> Boolean = { true }

    private var filteredItems: List<T> = ArrayList()

    open fun setItems(items: List<T>) {
        this.items = items

        filteredItems = items.filter(filter)
    }

    fun setFilter(filter: (T) -> Boolean) {
        this.filter = filter

        filteredItems = items.filter(filter)
    }

    override fun getItem(position: Int): T {
        return filteredItems[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return filteredItems.size
    }
}
