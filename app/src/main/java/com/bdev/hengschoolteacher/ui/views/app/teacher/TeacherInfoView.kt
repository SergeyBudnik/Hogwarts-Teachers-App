package com.bdev.hengschoolteacher.ui.views.app.teacher

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.services.staff.StaffMembersStorageService
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import com.bdev.hengschoolteacher.ui.activities.teacher.TeacherActivity
import kotlinx.android.synthetic.main.view_teacher_info.view.*
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EViewGroup

@EViewGroup(R.layout.view_teacher_info)
open class TeacherInfoView : LinearLayout {
    @Bean
    lateinit var staffMembersStorageService: StaffMembersStorageService

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    fun bind(
            teacherLogin: String,
            clickable: Boolean = true
    ) {
        teacherInfoNameView.text = staffMembersStorageService.getStaffMember(teacherLogin)?.person?.name ?: "?"

        setOnClickListener {
            if (clickable) {
                TeacherActivity.redirectToChild(
                        current = context as BaseActivity,
                        teacherLogin = teacherLogin
                )
            } else {
                /* Do nothing */
            }
        }
    }
}
