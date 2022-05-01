package com.bdev.hengschoolteacher.ui.views.app.lessons

import android.content.Context
import android.util.AttributeSet
import android.widget.RelativeLayout
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.ui.utils.ViewVisibilityUtils
import com.bdev.hengschoolteacher.ui.views.app.lesson.LessonRowViewData
import kotlinx.android.synthetic.main.view_lesson_item.view.*

class LessonItemView : RelativeLayout {
    companion object {
        const val REQUEST_CODE_LESSON = 1
    }

    init {
        inflate(context, R.layout.view_lesson_item, this)
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    fun bind(data: LessonRowViewData): LessonItemView {
        lessonItemRowView.bind(data = data)

        lessonItemTeacherView.text = data.staffMember?.person?.name ?: "?"
        lessonItemTeacherView.visibility =
            ViewVisibilityUtils.visibleElseGone(visible = data.showTeacher)

        return this
    }
}