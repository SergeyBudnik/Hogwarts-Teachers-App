package com.bdev.hengschoolteacher.ui.views.app.lesson

import android.content.Context
import android.util.AttributeSet
import android.widget.RelativeLayout
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.data.school.group.Lesson
import com.bdev.hengschoolteacher.service.LessonsService
import kotlinx.android.synthetic.main.view_lesson_info.view.*
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EViewGroup
import java.text.SimpleDateFormat
import java.util.*

@EViewGroup(R.layout.view_lesson_info)
open class LessonInfoView : RelativeLayout {
    @Bean
    lateinit var lessonsService: LessonsService

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    fun bind(lesson: Lesson) {
        lessonInfoDayOfWeekView.text = context.getString(lesson.day.shortNameId)
        lessonInfoDateView.text = SimpleDateFormat("dd.MM.yyyy", Locale.US).format(lessonsService.getLessonStartTime(lesson.id, 0))
        lessonInfoStartTimeView.text = context.getString(lesson.startTime.translationId)
        lessonInfoFinishTimeView.text = context.getString(lesson.finishTime.translationId)
    }
}
