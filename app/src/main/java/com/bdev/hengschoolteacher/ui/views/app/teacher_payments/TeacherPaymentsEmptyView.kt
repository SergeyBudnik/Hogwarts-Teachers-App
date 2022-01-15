package com.bdev.hengschoolteacher.ui.views.app.teacher_payments

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.bdev.hengschoolteacher.R

class TeacherPaymentsEmptyView : LinearLayout {
    init {
        View.inflate(context, R.layout.view_teacher_payments_empty, this)
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
}