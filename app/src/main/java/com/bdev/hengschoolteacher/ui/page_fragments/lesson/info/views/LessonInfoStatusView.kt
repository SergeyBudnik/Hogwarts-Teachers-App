package com.bdev.hengschoolteacher.ui.page_fragments.lesson.info.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.data.school.lesson.LessonStatusType
import com.bdev.hengschoolteacher.ui.views.branded.BrandedButtonView
import kotlinx.android.synthetic.main.view_lesson_info_status.view.*

class LessonInfoStatusView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr, defStyleRes) {
    init {
        View.inflate(context, R.layout.view_lesson_info_status, this)
    }

    fun init(
        lessonStatusType: LessonStatusType?,
        clickAction: () -> Unit
    ) {
        lessonInfoStatusButtonView.setText(
            getButtonText(lessonStatusType = lessonStatusType)
        )

        lessonInfoStatusButtonView.setStyle(
            getButtonStyle(lessonStatusType = lessonStatusType)
        )

        lessonInfoStatusButtonView.setOnClickListener {
            clickAction()
        }
    }

    // todo: move to strings
    private fun getButtonText(lessonStatusType: LessonStatusType?): String =
        when (lessonStatusType) {
            LessonStatusType.FINISHED -> "Занятие проведено"
            LessonStatusType.CANCELED -> "Занятие отменено"
            LessonStatusType.MOVED -> "Занятие перенесено"
            null -> "Отметить статус"
        }

    private fun getButtonStyle(lessonStatusType: LessonStatusType?): BrandedButtonView.Style =
        when (lessonStatusType) {
            null -> BrandedButtonView.Style.PRIMARY
            else -> BrandedButtonView.Style.DEFAULT
        }
}