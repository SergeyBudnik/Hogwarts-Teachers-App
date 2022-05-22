package com.bdev.hengschoolteacher.ui.fragments.lessons.views

import android.content.Context
import android.graphics.PorterDuff
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.data.school.lesson.LessonStatusType
import com.bdev.hengschoolteacher.data.school.student.Student
import com.bdev.hengschoolteacher.data.school.student.StudentAttendanceType
import com.bdev.hengschoolteacher.ui.fragments.lessons.LessonsFragmentItemData
import com.bdev.hengschoolteacher.ui.resources.AppResources
import com.bdev.hengschoolteacher.ui.utils.ViewVisibilityUtils.visibleElseGone
import kotlinx.android.synthetic.main.view_lessons_item.view.*
import kotlinx.android.synthetic.main.view_lessons_item.view.lessonRowLessonStatusView
import kotlinx.android.synthetic.main.view_lessons_item.view.lessonRowStatusView

class LessonsItemView : RelativeLayout {
    init {
        inflate(context, R.layout.view_lessons_item, this)
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    fun bind(data: LessonsFragmentItemData): LessonsItemView {
        lessonRowGroupIconView.bind(data.group)

        lessonRowStartTimeView.text = context.getString(data.lesson.startTime.translationId)
        lessonRowFinishTimeView.text = context.getString(data.lesson.finishTime.translationId)

        listOf(lessonRowStudent1View, lessonRowStudent2View, lessonRowStudent3View, lessonRowStudent4View, lessonRowStudent5View, lessonRowStudent6View)
            .forEachIndexed { index, it ->
                val studentAndAttendanceType = data.studentsToAttendanceType.getOrNull(index)

                val student = studentAndAttendanceType?.first
                val attendance = studentAndAttendanceType?.second

                setStudentIcon(student, attendance, it)
            }

        setLessonIcon(data)
        setLessonStatus(data)

        lessonItemTeacherView.text = data.staffMember?.person?.name ?: "?"
        lessonItemTeacherView.visibility = visibleElseGone(visible = data.showTeacher)

        return this
    }

    private fun setStudentIcon(student: Student?, attendanceType: StudentAttendanceType?, studentView: ImageView) {
        val icon = if (student == null) {
            R.drawable.ic_user_linear
        } else {
            R.drawable.ic_user_filled
        }

        val color = if (student == null) {
            R.color.fill_text_basic
        } else {
            when (attendanceType) {
                null -> R.color.fill_text_basic
                StudentAttendanceType.VISITED -> R.color.fill_text_basic_positive
                StudentAttendanceType.VALID_SKIP -> R.color.fill_text_basic_warning
                StudentAttendanceType.INVALID_SKIP -> R.color.fill_text_basic_negative
                StudentAttendanceType.FREE_LESSON -> R.color.fill_text_basic_action_link
            }
        }

        studentView.setImageDrawable(
            AppResources.getDrawable(
                context = context,
                drawableId = icon
            )
        )

        studentView.setColorFilter(
            AppResources.getColor(
                context = context,
                colorId = color
            )
        )
    }

    private fun setLessonIcon(data: LessonsFragmentItemData) {
        val iconId = if (data.isLessonFinished) {
            if (data.isLessonAttendanceFilled) {
                R.drawable.ic_lesson_status_finished
            } else {
                R.drawable.ic_lesson_status_unknown
            }
        } else {
            R.drawable.ic_lesson_status_not_finished
        }

        val colorId = if (data.isLessonFinished) {
            if (data.isLessonAttendanceFilled && data.lessonStatus != null) {
                R.color.fill_text_basic_positive
            } else {
                R.color.fill_text_basic_negative
            }
        } else {
            R.color.fill_text_basic_warning
        }

        lessonRowStatusView.setImageDrawable(
            AppResources.getDrawable(
                context = context,
                drawableId = iconId
            )
        )

        lessonRowStatusView.setColorFilter(
            AppResources.getColor(
                context = context,
                colorId = colorId
            ),
            PorterDuff.Mode.SRC_IN
        )
    }

    private fun setLessonStatus(data: LessonsFragmentItemData) {
        lessonRowLessonStatusView.visibility = if (data.lessonStatus != null) { View.VISIBLE } else { View.GONE }

        if (data.lessonStatus != null) {
            lessonRowLessonStatusView.text = when (data.lessonStatus.type) {
                LessonStatusType.FINISHED -> "Проведено"
                LessonStatusType.CANCELED -> "Отменено"
                LessonStatusType.MOVED -> "Перенесено"
            }
        }
    }
}