package com.bdev.hengschoolteacher.ui.activities.lesson.info.views

import android.content.Context
import android.graphics.PorterDuff
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.data.school.group.Lesson
import com.bdev.hengschoolteacher.data.school.student.Student
import com.bdev.hengschoolteacher.data.school.student.StudentAttendanceType
import com.bdev.hengschoolteacher.ui.activities.lesson.attendance.LessonAttendanceActivityData
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
            student: Student, lesson: Lesson, weekIndex: Int,
            currentDebt: Int, expectedDebt: Int,
            attendanceType: StudentAttendanceType?,
            goToStudentInformationAction: (Student) -> Unit,
            goToStudentPaymentAction: (Student) -> Unit,
            goToLessonAttendanceAction: (LessonAttendanceActivityData) -> Unit
    ): LessonInfoStudentItemView {
        lessonStudentItemNameView.text = student.person.name

        bindAttendance(
                student = student,
                lesson = lesson,
                weekIndex = weekIndex,
                attendanceType = attendanceType,
                goToLessonAttendanceAction = goToLessonAttendanceAction
        )

        bindPayment(
                student = student,
                goToStudentPaymentAction = goToStudentPaymentAction
        )

        bindDept(
                currentDebt = currentDebt,
                expectedDebt = expectedDebt
        )

        setOnClickListener {
            goToStudentInformationAction(student)
        }

        return this
    }

    private fun bindPayment(
            student: Student,
            goToStudentPaymentAction: (Student) -> Unit
    ) {
        lessonStudentItemPaymentView.setOnClickListener {
            goToStudentPaymentAction(student)
        }
    }

    private fun bindAttendance(
            student: Student, lesson: Lesson, weekIndex: Int,
            attendanceType: StudentAttendanceType?,
            goToLessonAttendanceAction: (LessonAttendanceActivityData) -> Unit
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
            goToLessonAttendanceAction(
                    LessonAttendanceActivityData(
                            lessonId = lesson.id,
                            studentLogin = student.login,
                            weekIndex = weekIndex
                    )
            )
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