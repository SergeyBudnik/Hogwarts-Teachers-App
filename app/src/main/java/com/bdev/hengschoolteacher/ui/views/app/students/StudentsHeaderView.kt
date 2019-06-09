package com.bdev.hengschoolteacher.ui.views.app.students

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import com.bdev.hengschoolteacher.ui.activities.students.StudentsGroupsListActivity_
import com.bdev.hengschoolteacher.ui.activities.students.StudentsListActivity_
import com.bdev.hengschoolteacher.ui.utils.RedirectBuilder.Companion.redirect
import kotlinx.android.synthetic.main.view_students_header.view.*
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.EViewGroup

@EViewGroup(R.layout.view_students_header)
open class StudentsHeaderView(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {
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
        val ta = context.obtainStyledAttributes(attrs, R.styleable.StudentsHeaderView, 0, 0)

        try {
            item = Item.findById(ta.getInt(R.styleable.StudentsHeaderView_students_item, 1))
        } finally {
            ta.recycle()
        }
    }

    @AfterViews
    fun init() {
        studentsHeaderListView.setActive(item == Item.LIST)
        studentsHeaderGroupsView.setActive(item == Item.GROUPS)

        studentsHeaderListView.setOnClickListener {
            redirect(context as BaseActivity).to(StudentsListActivity_::class.java).goAndCloseCurrent()
        }

        studentsHeaderGroupsView.setOnClickListener {
            redirect(context as BaseActivity).to(StudentsGroupsListActivity_::class.java).goAndCloseCurrent()
        }
    }
}

