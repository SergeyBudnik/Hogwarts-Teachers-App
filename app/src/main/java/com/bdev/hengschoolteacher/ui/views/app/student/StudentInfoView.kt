package com.bdev.hengschoolteacher.ui.views.app.student

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.data.school.student.Student
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
//                StudentInformationPageFragment.redirectToChild(
//                        current = context as BasePageFragment,
//                        studentLogin = student.login
//                )
            } else {
                /* Do nothing */
            }
        }
    }
}
