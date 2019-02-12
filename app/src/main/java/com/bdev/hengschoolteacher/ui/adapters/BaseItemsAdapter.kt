package com.bdev.hengschoolteacher.ui.adapters

import android.content.Context
import android.widget.BaseAdapter

abstract class BaseItemsAdapter<T>(
        protected val context: Context,
        private val items: List<T>
) : BaseAdapter() {
    override fun getItem(position: Int): T {
        return items[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return items.size
    }
}
