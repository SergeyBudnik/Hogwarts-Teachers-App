package com.bdev.hengschoolteacher.ui.page_fragments.lesson.attendance.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.data.school.student.StudentAttendanceType
import kotlinx.android.synthetic.main.view_lesson_attendance_button.view.*

class LessonAttendanceButtonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr, defStyleRes) {
    init {
        View.inflate(context, R.layout.view_lesson_attendance_button, this)
    }

    fun init(
        buttonAttendanceType: StudentAttendanceType,
        actualAttendanceType: StudentAttendanceType?,
        progressAttendanceType: StudentAttendanceType?,
        clickAction: () -> Unit
    ) {
        lessonAttendanceButtonView.setButtonText(
            getButtonText(buttonAttendanceType = buttonAttendanceType)
        )

        if (progressAttendanceType != null) {
            if (buttonAttendanceType == progressAttendanceType) {
                lessonAttendanceButtonView.setButtonInProgressIcon()
            } else {
                lessonAttendanceButtonView.hideButtonIcon()
            }
        } else {
            lessonAttendanceButtonView.setButtonIcon(
                iconId = R.drawable.ic_ok,
                colorId = getButtonColorId(
                    actualAttendanceType = actualAttendanceType,
                    buttonAttendanceType = buttonAttendanceType
                )
            )
        }

        lessonAttendanceButtonView.setOnClickListener {
            if (progressAttendanceType == null) {
                clickAction()
            }
        }
    }

    private fun getButtonColorId(
        actualAttendanceType: StudentAttendanceType?,
        buttonAttendanceType: StudentAttendanceType
    ): Int =
        if (actualAttendanceType == buttonAttendanceType) {
            when (buttonAttendanceType) {
                StudentAttendanceType.VISITED -> R.color.fill_text_basic_positive
                StudentAttendanceType.VALID_SKIP -> R.color.fill_text_basic_warning
                StudentAttendanceType.INVALID_SKIP -> R.color.fill_text_basic_negative
                StudentAttendanceType.FREE_LESSON -> R.color.fill_text_basic_action_link
            }
        } else {
            R.color.fill_text_basic_light
        }

    // todo: migrate to strings
    private fun getButtonText(buttonAttendanceType: StudentAttendanceType): String =
        when (buttonAttendanceType) {
            StudentAttendanceType.VISITED -> "Посетил"
            StudentAttendanceType.VALID_SKIP -> "Уважительный пропуск"
            StudentAttendanceType.INVALID_SKIP -> "Неуважительный пропуск"
            StudentAttendanceType.FREE_LESSON -> "Бесплатное занятие"
        }
}