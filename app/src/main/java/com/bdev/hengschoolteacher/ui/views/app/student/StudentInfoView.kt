package com.bdev.hengschoolteacher.ui.views.app.student

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.data.school.student.Student
import kotlinx.android.synthetic.main.view_student_info.view.*
import org.androidannotations.annotations.EViewGroup

@EViewGroup(R.layout.view_student_info)
open class StudentInfoView : LinearLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    fun bind(student: Student) {
        studentInfoNameView.text = student.name
    }
}
