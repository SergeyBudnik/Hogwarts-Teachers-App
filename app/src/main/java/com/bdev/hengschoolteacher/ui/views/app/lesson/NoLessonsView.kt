package com.bdev.hengschoolteacher.ui.views.app.lesson

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.bdev.hengschoolteacher.R
import kotlinx.android.synthetic.main.view_no_lessons.view.*
import org.androidannotations.annotations.EViewGroup

@EViewGroup(R.layout.view_no_lessons)
open class NoLessonsView : LinearLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    fun bind(disableFilterAction: () -> Unit) {
        profileLessonsNoLessonsDisableFilterView.setOnClickListener {
            disableFilterAction()
        }
    }
}
