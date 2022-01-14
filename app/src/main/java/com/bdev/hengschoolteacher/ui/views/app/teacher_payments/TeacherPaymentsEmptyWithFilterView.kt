package com.bdev.hengschoolteacher.ui.views.app.teacher_payments

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.bdev.hengschoolteacher.R
import kotlinx.android.synthetic.main.view_teacher_payments_empty_with_filter.view.*

class TeacherPaymentsEmptyWithFilterView : LinearLayout {
    init {
        View.inflate(context, R.layout.view_teacher_payments_empty_with_filter, this)
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    fun bind(disableFilterAction: () -> Unit) {
        teacherPaymentsEmptyWithFilterDisableFilterView.setOnClickListener {
            disableFilterAction.invoke()
        }
    }
}