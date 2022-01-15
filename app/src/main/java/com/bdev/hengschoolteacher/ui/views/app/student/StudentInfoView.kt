package com.bdev.hengschoolteacher.ui.views.app.student

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.data.school.student.Student
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import com.bdev.hengschoolteacher.ui.activities.student.StudentInformationActivity
import kotlinx.android.synthetic.main.view_student_info.view.*

class StudentInfoView : LinearLayout {
    init {
        View.inflate(context, R.layout.view_student_info, this)
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    fun bind(
            student: Student,
            clickable: Boolean = true
    ) {
        studentInfoNameView.text = student.person.name

        setOnClickListener {
            if (clickable) {
                StudentInformationActivity.redirectToChild(
                        current = context as BaseActivity,
                        studentLogin = student.login
                )
            } else {
                /* Do nothing */
            }
        }
    }
}
