package com.bdev.hengschoolteacher.ui.activities.student

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.data.school.Month
import com.bdev.hengschoolteacher.data.school.student.Student
import com.bdev.hengschoolteacher.data.school.student.StudentAttendance
import com.bdev.hengschoolteacher.service.*
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import kotlinx.android.synthetic.main.activity_student_statistics.*
import kotlinx.android.synthetic.main.view_student_month_attendance.view.*
import org.androidannotations.annotations.*
import java.util.*

@EViewGroup(R.layout.view_student_month_attendance)
open class StudentMonthAttendanceView : LinearLayout {
    @Bean
    lateinit var lessonsService: LessonsService
    @Bean
    lateinit var studentsAttendancesService: StudentsAttendancesService
    @Bean
    lateinit var studentsPaymentsService: StudentsPaymentsService

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    fun bind(student: Student, monthOffset: Int) {
        val currentMonthIndex = Calendar.getInstance().get(Calendar.MONTH) + monthOffset

        studentAttendancesMonthView.text = resources.getString(Month.findByIndex(currentMonthIndex).nameId)

        val amountOfLessons = lessonsService.getLessonsAmountInMonth(student.studentGroups[0].groupId, currentMonthIndex)
        val attendances = studentsAttendancesService.getMonthlyAttendances(student, currentMonthIndex)
        val payment = studentsPaymentsService.getMonthPayments(student, currentMonthIndex)

        paymentView.text = "$payment Р"

        listOf(
                studentAttendance0View, studentAttendance1View, studentAttendance2View, studentAttendance3View, studentAttendance4View,
                studentAttendance5View, studentAttendance6View, studentAttendance7View, studentAttendance8View, studentAttendance9View
        ).forEachIndexed { index, view ->
            val drawableId = if (index >= amountOfLessons) {
                R.drawable.ic_user_linear
            } else {
                R.drawable.ic_user_filled
            }

            val colorId = if (index >= amountOfLessons) {
                R.color.fill_text_basic
            } else {
                if (index >= attendances.size) {
                    R.color.fill_text_basic
                } else {
                    when (attendances[index].type) {
                        StudentAttendance.Type.VISITED -> R.color.fill_text_basic_positive
                        StudentAttendance.Type.VALID_SKIP -> R.color.fill_text_basic_warning
                        StudentAttendance.Type.INVALID_SKIP -> R.color.fill_text_basic_negative
                    }
                }
            }

            view.setImageDrawable(resources.getDrawable(drawableId))
            view.setColorFilter(resources.getColor(colorId))
        }
    }
}

@SuppressLint("Registered")
@EActivity(R.layout.activity_student_statistics)
open class StudentStatisticsActivity : BaseActivity() {
    companion object {
        const val EXTRA_STUDENT_ID = "EXTRA_STUDENT_ID"
    }

    @Extra(EXTRA_STUDENT_ID)
    @JvmField
    var studentId = 0L

    @Bean
    lateinit var studentsService: StudentsService

    @AfterViews
    fun init() {
        studentPaymentHeaderView.setLeftButtonAction { doFinish() }

        val student = studentsService.getStudent(studentId) ?: throw RuntimeException()

        studentPaymentSecondaryHeaderView.bind(student)

        studentPaymentInfoView.bind(student)

        currentMonthAttendanceView.bind(student, 0)
        previousMonthAttendanceView.bind(student, -1)
    }

    override fun onBackPressed() {
        doFinish()
    }

    private fun doFinish() {
        finish()
        overridePendingTransition(R.anim.slide_close_enter, R.anim.slide_close_exit)
    }
}
