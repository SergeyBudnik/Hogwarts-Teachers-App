package com.bdev.hengschoolteacher.ui.views.app.lesson

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.bdev.hengschoolteacher.R
import kotlinx.android.synthetic.main.view_no_lessons.view.*

class NoLessonsView : LinearLayout {
    init {
        View.inflate(context, R.layout.view_no_lessons, this)
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    fun bind(disableFilterAction: () -> Unit) {
        profileLessonsNoLessonsDisableFilterView.setOnClickListener {
            disableFilterAction()
        }
    }
}
