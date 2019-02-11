package com.bdev.hengschoolteacher.ui.views.app.teacher

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.service.TeachersService
import kotlinx.android.synthetic.main.view_teacher_info.view.*
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EViewGroup
import java.lang.RuntimeException

@EViewGroup(R.layout.view_teacher_info)
open class TeacherInfoView : LinearLayout {
    @Bean
    lateinit var teachersService: TeachersService

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    fun bind(teacherId: Long) {
        teacherInfoNameView.text = teachersService.getTeacherById(teacherId)?.name ?: throw RuntimeException()
    }
}
