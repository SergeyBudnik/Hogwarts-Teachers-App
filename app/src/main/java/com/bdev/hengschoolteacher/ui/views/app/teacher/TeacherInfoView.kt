package com.bdev.hengschoolteacher.ui.views.app.teacher

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.data.school.staff.StaffMember
import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragment
import com.bdev.hengschoolteacher.ui.page_fragments.teacher.TeacherPageFragment
import kotlinx.android.synthetic.main.view_teacher_info.view.*

class TeacherInfoView : LinearLayout {
    init {
        View.inflate(context, R.layout.view_teacher_info, this)
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    fun bind(
            teacher: StaffMember?,
            clickable: Boolean = true
    ) {
        teacherInfoNameView.text = teacher?.person?.name ?: "?"

        setOnClickListener {
            if (clickable && teacher != null) {
//                TeacherPageFragment.redirectToChild(
//                        current = context as BasePageFragment,
//                        teacherLogin = teacher.login
//                )
            } else {
                /* Do nothing */
            }
        }
    }
}
