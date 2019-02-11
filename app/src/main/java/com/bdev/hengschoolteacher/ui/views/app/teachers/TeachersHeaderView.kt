package com.bdev.hengschoolteacher.ui.views.app.teachers

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import com.bdev.hengschoolteacher.ui.activities.teachers.TeachersListActivity_
import com.bdev.hengschoolteacher.ui.utils.RedirectUtils.Companion.redirect
import kotlinx.android.synthetic.main.view_header_teachers.view.*
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.EViewGroup

@EViewGroup(R.layout.view_header_teachers)
open class TeachersHeaderView(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {
    private enum class Item(val id: Int) {
        LIST(1);

        companion object {
            fun findById(id: Int): Item {
                return values().find { it.id == id } ?: throw RuntimeException()
            }
        }
    }

    private val item: Item

    init {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.TeachersHeaderView, 0, 0)

        try {
            item = Item.findById(ta.getInt(R.styleable.TeachersHeaderView_teachers_item, 1))
        } finally {
            ta.recycle()
        }
    }

    @AfterViews
    fun init() {
        teachersHeaderLessonsView.setActive(item == Item.LIST)

        teachersHeaderLessonsView.setOnClickListener {
            redirect(context as BaseActivity).to(TeachersListActivity_::class.java).goAndCloseCurrent()
        }
    }
}

