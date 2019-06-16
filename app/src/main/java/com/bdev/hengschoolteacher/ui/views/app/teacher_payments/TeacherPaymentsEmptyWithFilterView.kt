package com.bdev.hengschoolteacher.ui.views.app.teacher_payments

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.bdev.hengschoolteacher.R
import kotlinx.android.synthetic.main.view_teacher_payments_empty_with_filter.view.*
import org.androidannotations.annotations.EViewGroup

@EViewGroup(R.layout.view_teacher_payments_empty_with_filter)
open class TeacherPaymentsEmptyWithFilterView : LinearLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    fun bind(disableFilterAction: () -> Unit) {
        teacherPaymentsEmptyWithFilterDisableFilterView.setOnClickListener {
            disableFilterAction.invoke()
        }
    }
}