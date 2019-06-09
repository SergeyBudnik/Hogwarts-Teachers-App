package com.bdev.hengschoolteacher.ui.activities.teacher

import android.annotation.SuppressLint
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.service.teacher.TeacherStorageService
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import com.bdev.hengschoolteacher.ui.utils.RedirectBuilder
import com.bdev.hengschoolteacher.ui.views.branded.BrandedPhoneView_
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

        fun redirect(current: BaseActivity, teacherId: Long): RedirectBuilder {
            return RedirectBuilder
                    .redirect(current)
                    .to(TeacherActivity_::class.java)
                    .withExtra(EXTRA_TEACHER_ID, teacherId)
        }
    }

    @Extra(EXTRA_TEACHER_ID)
    @JvmField
    var teacherId = 0L

    @Bean
    lateinit var teacherStorageService: TeacherStorageService

    @AfterViews
    fun init() {
        teacherHeaderView.setLeftButtonAction { doFinish() }

        val teacher = teacherStorageService.getTeacherById(teacherId) ?: throw RuntimeException()

        teacherNameView.text = teacher.name

        teacherPhonesContainerView.removeAllViews()

        teacher.phones.forEach { phone ->
            teacherPhonesContainerView.addView(BrandedPhoneView_.build(this).bind(phone))
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
