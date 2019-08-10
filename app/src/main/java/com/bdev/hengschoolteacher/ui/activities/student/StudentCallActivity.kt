package com.bdev.hengschoolteacher.ui.activities.student

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.widget.RelativeLayout
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.service.StudentsService
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import com.bdev.hengschoolteacher.ui.utils.PhoneUtils
import com.bdev.hengschoolteacher.ui.utils.RedirectBuilder
import com.bdev.hengschoolteacher.ui.views.app.AppLayoutView
import kotlinx.android.synthetic.main.activity_student_call.*
import kotlinx.android.synthetic.main.view_item_student_phone.view.*
import org.androidannotations.annotations.*

@EViewGroup(R.layout.view_item_student_phone)
open class StudentPhoneItemView : RelativeLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    fun bind(phone: String) : StudentPhoneItemView {
        studentPhoneView.text = phone

        setOnClickListener { PhoneUtils.call(
                context as BaseActivity,
                phone
        ) }

        return this
    }
}

@SuppressLint("Registered")
@EActivity(R.layout.activity_student_call)
open class StudentCallActivity : BaseActivity() {
    companion object {
        const val EXTRA_STUDENT_ID = "EXTRA_STUDENT_ID"

        fun redirectToChild(current: BaseActivity, studentId: Long) {
            RedirectBuilder
                    .redirect(current)
                    .to(StudentCallActivity_::class.java)
                    .withExtra(EXTRA_STUDENT_ID, studentId)
                    .withAnim(R.anim.slide_open_enter, R.anim.slide_open_exit)
                    .go()
        }
    }

    @Bean
    lateinit var studentsService: StudentsService

    @Extra(EXTRA_STUDENT_ID)
    @JvmField
    var studentId: Long = 0

    @AfterViews
    fun init() {
        studentCallHeaderView.setLeftButtonAction { doFinish() }

        val student = studentsService.getStudent(studentId) ?: throw RuntimeException()

        studentCallNameView.text = student.name
        studentCallPhonesLayoutView.removeAllViews()

        student.phones.forEach { studentCallPhonesLayoutView.addView(StudentPhoneItemView_.build(this).bind(it)) }
    }

    override fun onBackPressed() {
        doFinish()
    }

    private fun doFinish() {
        finish()
        overridePendingTransition(R.anim.slide_close_enter, R.anim.slide_close_exit)
    }

    override fun getAppLayoutView(): AppLayoutView? {
        return null
    }
}
