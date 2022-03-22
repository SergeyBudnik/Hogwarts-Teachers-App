package com.bdev.hengschoolteacher.ui.views.app.students

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragment
import kotlinx.android.synthetic.main.view_students_header.view.*

class StudentsHeaderView(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {
    private enum class Item(val id: Int) {
        LIST(1), GROUPS(2);

        companion object {
            fun findById(id: Int): Item {
                return values().find { it.id == id } ?: throw RuntimeException()
            }
        }
    }

    private val item: Item

    init {
        View.inflate(context, R.layout.view_students_header, this)

        val ta = context.obtainStyledAttributes(attrs, R.styleable.StudentsHeaderView, 0, 0)

        try {
            item = Item.findById(ta.getInt(R.styleable.StudentsHeaderView_students_item, 1))
        } finally {
            ta.recycle()
        }

        studentsHeaderListView.bind(
                active = item == Item.LIST,
                hasAlert = false,
                clickAction = {
                    // StudentsListPageFragment.redirectToSibling(context as BasePageFragment)
                }
        )

        studentsHeaderGroupsView.bind(
                active = item == Item.GROUPS,
                hasAlert = false,
                clickAction = {
                    // StudentsGroupsListPageFragment.redirectToSibling(context as BasePageFragment)
                }
        )
    }
}

