package com.bdev.hengschoolteacher.ui.activities.lesson.info.views

import android.content.Context
import android.graphics.PorterDuff
import android.util.AttributeSet
import android.widget.RelativeLayout
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.data.school.group.Lesson
import com.bdev.hengschoolteacher.data.school.student.Student
import com.bdev.hengschoolteacher.data.school.student.StudentAttendanceType
import com.bdev.hengschoolteacher.service.StudentPaymentsDeptService
import com.bdev.hengschoolteacher.service.StudentsPaymentsService
import com.bdev.hengschoolteacher.service.student_attendance.StudentsAttendancesProviderService
import com.bdev.hengschoolteacher.ui.activities.lesson.attendance.LessonAttendanceActivityData
import com.bdev.hengschoolteacher.ui.resources.AppResources
import com.bdev.hengschoolteacher.ui.utils.ViewVisibilityUtils.visibleElseGone
import kotlinx.android.synthetic.main.view_item_lesson_info_student.view.*
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EViewGroup

@EViewGroup(R.layout.view_item_lesson_info_student)
open class LessonInfoStudentItemView : RelativeLayout {
    @Bean
    lateinit var studentsAttendanceProviderService: StudentsAttendancesProviderService
    @Bean
    lateinit var studentsPaymentsService: StudentsPaymentsService

    @Bean
    lateinit var studentPaymentsDeptService: StudentPaymentsDeptService

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    fun bind(
            student: Student, lesson: Lesson, weekIndex: Int,
            goToStudentInformationAction: (Student) -> Unit,
            goToStudentPaymentAction: (Student) -> Unit,
            goToLessonAttendanceAction: (LessonAttendanceActivityData) -> Unit
    ): LessonInfoStudentItemView {
        lessonStudentItemNameView.text = student.person.name

        bindAttendance(
                student = student,
                lesson = lesson,
                weekIndex = weekIndex,
                goToLessonAttendanceAction = goToLessonAttendanceAction
        )

        bindPayment(
                student = student,
                goToStudentPaymentAction = goToStudentPaymentAction
        )

        bindDept(
                student = student
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
            goToLessonAttendanceAction: (LessonAttendanceActivityData) -> Unit
    ) {
        val attendanceType = studentsAttendanceProviderService.getAttendance(lesson.id, student.login, weekIndex)

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

    private fun bindDept(student: Student) {
        val dept = studentPaymentsDeptService.getStudentDept(student.login)

        lessonStudentItemNoDeptView.visibility = visibleElseGone(visible = dept <= 0)
        lessonStudentItemDeptView.visibility = visibleElseGone(visible = dept > 0)

        lessonStudentItemDeptView.text = "Долг: $dept Р"
    }
}