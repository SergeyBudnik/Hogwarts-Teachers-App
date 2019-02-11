package com.bdev.hengschoolteacher.ui.activities

import android.annotation.SuppressLint
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.service.TeachersPaymentService
import com.bdev.hengschoolteacher.service.TeachersService
import com.bdev.hengschoolteacher.ui.views.app.student.StudentPhoneItemView_
import kotlinx.android.synthetic.main.activity_teacher.*
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EActivity
import org.androidannotations.annotations.Extra

@SuppressLint("Registered")
@EActivity(R.layout.activity_teacher)
open class TeacherActivity : BaseActivity() {
    companion object {
        const val EXTRA_TEACHER_ID = "EXTRA_TEACHER_ID"
    }

    @Extra(EXTRA_TEACHER_ID)
    @JvmField
    var teacherId = 0L

    @Bean
    lateinit var teachersService: TeachersService
    @Bean
    lateinit var teachersPaymentService: TeachersPaymentService

    @AfterViews
    fun init() {
        teacherHeaderView.setLeftButtonAction { doFinish() }

        val teacher = teachersService.getTeacherById(teacherId) ?: throw RuntimeException()

        teacherNameView.text = teacher.name

        teacherPhonesContainerView.removeAllViews()

        teacher.phones.forEach { phone ->
            teacherPhonesContainerView.addView(StudentPhoneItemView_.build(this).bind(phone))
        }

        teacherIncomeView.text = "-${teachersPaymentService.getIncome(teacherId)} Р"
        // teacherOutcomeView.text = "+${teachersPaymentService.getOutcome(teacherId)} Р"
    }

    override fun onBackPressed() {
        doFinish()
    }

    private fun doFinish() {
        finish()
        overridePendingTransition(R.anim.slide_close_enter, R.anim.slide_close_exit)
    }
}
