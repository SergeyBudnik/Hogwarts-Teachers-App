package com.bdev.hengschoolteacher.ui.activities.student

import android.annotation.SuppressLint
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.service.StudentsService
import com.bdev.hengschoolteacher.ui.activities.*
import com.bdev.hengschoolteacher.ui.activities.lesson.LessonStudentAttendanceActivity
import com.bdev.hengschoolteacher.ui.utils.RedirectUtils.Companion.redirect
import kotlinx.android.synthetic.main.activity_student_information.*
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EActivity
import org.androidannotations.annotations.Extra

@SuppressLint("Registered")
@EActivity(R.layout.activity_student_information)
open class StudentInformationActivity : BaseActivity() {
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
        studentInformationHeaderView.setLeftButtonAction { doFinish() }

        val student = studentsService.getStudent(studentId) ?: throw RuntimeException()

        studentInformationSecondaryHeaderView.bind(student)

        studentNameView.text = student.name

        studentInformationCallView.setOnClickListener {
            redirect(this)
                    .to(StudentCallActivity_::class.java)
                    .withExtra(LessonStudentAttendanceActivity.EXTRA_STUDENT_ID, student.id)
                    .withAnim(R.anim.slide_open_enter, R.anim.slide_open_exit)
                    .go()
        }

        studentInformationPaymentView.setOnClickListener {
            redirect(this)
                    .to(StudentPaymentActivity_::class.java)
                    .withExtra(StudentPaymentActivity.EXTRA_STUDENT_ID, student.id)
                    .withAnim(R.anim.slide_open_enter, R.anim.slide_open_exit)
                    .go()
        }
    }

    override fun onBackPressed() {
        doFinish()
    }

    private fun doFinish() {
        finish()
        overridePendingTransition(R.anim.slide_close_enter, R.anim.slide_close_exit)
    }
}
