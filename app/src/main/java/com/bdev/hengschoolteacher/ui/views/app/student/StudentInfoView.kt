package com.bdev.hengschoolteacher.ui.views.app.student

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.data.school.student.Student
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import com.bdev.hengschoolteacher.ui.activities.student.StudentInformationActivity
import kotlinx.android.synthetic.main.view_student_info.view.*
import org.androidannotations.annotations.EViewGroup

@EViewGroup(R.layout.view_student_info)
open class StudentInfoView : LinearLayout {
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
