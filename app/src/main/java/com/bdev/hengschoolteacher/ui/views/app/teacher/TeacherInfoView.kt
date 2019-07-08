package com.bdev.hengschoolteacher.ui.views.app.teacher

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.service.teacher.TeacherStorageService
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import com.bdev.hengschoolteacher.ui.activities.teacher.TeacherActivity
import kotlinx.android.synthetic.main.view_teacher_info.view.*
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EViewGroup

@EViewGroup(R.layout.view_teacher_info)
open class TeacherInfoView : LinearLayout {
    @Bean
    lateinit var teacherStorageService: TeacherStorageService

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    fun bind(
            teacherId: Long,
            clickable: Boolean = true
    ) {
        teacherInfoNameView.text = teacherStorageService.getTeacherById(teacherId)?.name ?: "?"

        setOnClickListener {
            if (clickable) {
                TeacherActivity.redirectToChild(
                        current = context as BaseActivity,
                        teacherId = teacherId
                )
            } else {
                /* Do nothing */
            }
        }
    }
}
