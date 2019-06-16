package com.bdev.hengschoolteacher.ui.views.app.teacher_payments

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.bdev.hengschoolteacher.R
import org.androidannotations.annotations.EViewGroup

@EViewGroup(R.layout.view_teacher_payments_empty)
open class TeacherPaymentsEmptyView : LinearLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
}