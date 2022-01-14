package com.bdev.hengschoolteacher.ui.views.app.lesson

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.data.school.group.Lesson
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import com.bdev.hengschoolteacher.ui.activities.teacher.TeacherActivity
import com.bdev.hengschoolteacher.ui.utils.TimeFormatUtils
import kotlinx.android.synthetic.main.view_lesson_info.view.*

class LessonInfoView : RelativeLayout {
    init {
        View.inflate(context, R.layout.view_lesson_info, this)
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    fun bind(lesson: Lesson, lessonStartTime: Long, teacherName: String, teacherSurname: String) {
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

        lessonInfoTeacherNameView.text = teacherName
        lessonInfoTeacherSurnameView.text = teacherSurname
    }
}
