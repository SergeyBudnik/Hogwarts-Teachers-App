package com.bdev.hengschoolteacher.ui.page_fragments.lesson.info.views

import android.content.Context
import android.graphics.PorterDuff
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.data.school.student.StudentAttendanceType
import com.bdev.hengschoolteacher.ui.resources.AppResources
import com.bdev.hengschoolteacher.ui.utils.ViewVisibilityUtils.visibleElseGone
import kotlinx.android.synthetic.main.view_item_lesson_info_student.view.*

class LessonInfoStudentItemView : RelativeLayout {
    init {
        View.inflate(context, R.layout.view_item_lesson_info_student, this)
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    fun bind(
        name: String,
        login: String,
        attendanceType: StudentAttendanceType?,
        currentDebt: Int,
        expectedDebt: Int,
        goToStudentInformationAction: (String) -> Unit,
        goToStudentPaymentAction: (String) -> Unit,
        goToLessonAttendanceAction: (String) -> Unit
    ): LessonInfoStudentItemView {
        lessonStudentItemNameView.text = name

        bindAttendance(
            login = login,
            attendanceType = attendanceType,
            goToLessonAttendanceAction = goToLessonAttendanceAction
        )

        bindPayment(
            login = login,
            goToStudentPaymentAction = goToStudentPaymentAction
        )

        bindDept(
            currentDebt = currentDebt,
            expectedDebt = expectedDebt
        )

        setOnClickListener {
            goToStudentInformationAction(login)
        }

        return this
    }

    private fun bindPayment(
        login: String,
        goToStudentPaymentAction: (String) -> Unit
    ) {
        lessonStudentItemPaymentView.setOnClickListener {
            goToStudentPaymentAction(login)
        }
    }

    private fun bindAttendance(
        login: String,
        attendanceType: StudentAttendanceType?,
        goToLessonAttendanceAction: (String) -> Unit
    ) {
        val colorId = when (attendanceType) {
            null -> R.color.fill_text_basic
            StudentAttendanceType.VISITED -> R.color.fill_text_basic_positive
            StudentAttendanceType.VALID_SKIP -> R.color.fill_text_basic_warning
            StudentAttendanceType.INVALID_SKIP -> R.color.fill_text_basic_negative
            StudentAttendanceType.FREE_LESSON -> R.color.fill_text_basic_action_link
        }

        lessonStudentItemAttendanceView.setColorFilter(
            AppResources.getColor(
                context = context,
                colorId = colorId
            ),
            PorterDuff.Mode.SRC_IN
        )

        lessonStudentItemAttendanceView.setOnClickListener {
            goToLessonAttendanceAction(login)
        }
    }

    private fun bindDept(currentDebt: Int, expectedDebt: Int) {
        lessonStudentItemNoDeptView.visibility = visibleElseGone(visible = expectedDebt <= 0)

        lessonStudentItemDeptView.visibility = visibleElseGone(visible = expectedDebt > 0)
        lessonStudentItemExpectedDebtView.visibility = visibleElseGone(visible = expectedDebt > 0)

        lessonStudentItemDeptView.text = "Долг: $currentDebt Р"
        lessonStudentItemExpectedDebtView.text = "Ожидаемый долг: $expectedDebt Р"
    }
}