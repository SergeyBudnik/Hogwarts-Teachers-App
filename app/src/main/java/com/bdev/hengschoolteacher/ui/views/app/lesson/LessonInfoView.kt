package com.bdev.hengschoolteacher.ui.views.app.lesson

import android.content.Context
import android.util.AttributeSet
import android.widget.RelativeLayout
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.data.school.group.Lesson
import com.bdev.hengschoolteacher.service.LessonsService
import com.bdev.hengschoolteacher.service.staff.StaffMembersStorageService
import com.bdev.hengschoolteacher.service.teacher.TeacherInfoService
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import com.bdev.hengschoolteacher.ui.activities.teacher.TeacherActivity
import com.bdev.hengschoolteacher.ui.utils.TimeFormatUtils
import kotlinx.android.synthetic.main.view_lesson_info.view.*
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EViewGroup

@EViewGroup(R.layout.view_lesson_info)
open class LessonInfoView : RelativeLayout {
    @Bean
    lateinit var lessonsService: LessonsService
    @Bean
    lateinit var staffMembersStorageService: StaffMembersStorageService
    @Bean
    lateinit var teacherInfoService: TeacherInfoService

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    fun bind(lesson: Lesson, weekIndex: Int) {
        val lessonStartTime = lessonsService.getLessonStartTime(lesson.id, weekIndex)

        lessonInfoDayOfWeekView.text = context.getString(lesson.day.shortNameId)
        lessonInfoDateView.text = TimeFormatUtils.formatOnlyDate(lessonStartTime)
        lessonInfoStartTimeView.text = context.getString(lesson.startTime.translationId)
        lessonInfoFinishTimeView.text = context.getString(lesson.finishTime.translationId)

        lessonInfoTeacherView.setOnClickListener {
            TeacherActivity.redirectToChild(
                    current = context as BaseActivity,
                    teacherLogin = lesson.teacherLogin
            )
        }

        val teacher = staffMembersStorageService.getStaffMember(lesson.teacherLogin)

        lessonInfoTeacherNameView.text = teacher?.let { teacherInfoService.getTeachersName(it) } ?: ""
        lessonInfoTeacherSurnameView.text = teacher?.let { teacherInfoService.getTeachersSurname(it) } ?: ""
    }
}
