package com.bdev.hengschoolteacher.ui.page_fragments.lesson.status.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.data.school.lesson.LessonStatusType
import kotlinx.android.synthetic.main.view_lesson_status_button.view.*

class LessonStatusButtonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr, defStyleRes) {
    init {
        View.inflate(context, R.layout.view_lesson_status_button, this)
    }

    fun init(
        buttonStatusType: LessonStatusType,
        actualStatusType: LessonStatusType?,
        progressStatusType: LessonStatusType?,
        clickAction: () -> Unit
    ) {
        lessonStatusButtonView.setButtonText(
            getButtonText(statusType = buttonStatusType)
        )

        if (progressStatusType != null) {
            if (progressStatusType == buttonStatusType) {
                lessonStatusButtonView.setButtonInProgressIcon()
            } else {
                lessonStatusButtonView.hideButtonIcon()
            }
        } else {
            lessonStatusButtonView.setButtonIcon(
                R.drawable.ic_ok,
                if (actualStatusType == buttonStatusType) {
                    when (buttonStatusType) {
                        LessonStatusType.FINISHED -> R.color.fill_text_basic_positive
                        LessonStatusType.CANCELED -> R.color.fill_text_basic_negative
                        LessonStatusType.MOVED -> R.color.fill_text_basic_negative
                    }
                } else {
                    R.color.fill_text_basic_light
                }
            )
        }

        lessonStatusButtonView.setOnClickListener {
            clickAction()
        }
    }

    private fun getButtonText(statusType: LessonStatusType): String =
        when (statusType) {
            LessonStatusType.FINISHED -> "Проведено"
            LessonStatusType.CANCELED -> "Отменено"
            LessonStatusType.MOVED -> "Перенесено"
        }
}