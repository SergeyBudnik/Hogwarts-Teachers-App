package com.bdev.hengschoolteacher.ui.utils

import android.widget.ListView

object ListUtils {
    fun getListScrollState(listView: ListView): Pair<Int, Int> {
        val index = listView.firstVisiblePosition
        val top = listView.getChildAt(0)?.let { it.top - listView.paddingTop } ?: 0

        return Pair(index, top)
    }
}
