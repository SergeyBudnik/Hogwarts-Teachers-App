package com.bdev.hengschoolteacher.ui.activities.teacher

import android.annotation.SuppressLint
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.service.staff.StaffMembersStorageService
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import com.bdev.hengschoolteacher.ui.utils.RedirectBuilder
import com.bdev.hengschoolteacher.ui.views.app.AppLayoutView
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
        const val EXTRA_TEACHER_LOGIN = "EXTRA_TEACHER_LOGIN"

        fun redirectToChild(current: BaseActivity, teacherLogin: String) {
            RedirectBuilder
                    .redirect(current)
                    .to(TeacherActivity_::class.java)
                    .withExtra(EXTRA_TEACHER_LOGIN, teacherLogin)
                    .withAnim(R.anim.slide_open_enter, R.anim.slide_open_exit)
                    .go()
        }
    }

    @Extra(EXTRA_TEACHER_LOGIN)
    lateinit var teacherLogin: String

    @Bean
    lateinit var staffMembersStorageService: StaffMembersStorageService

    @AfterViews
    fun init() {
        teacherHeaderView.setLeftButtonAction { doFinish() }

        val teacher = staffMembersStorageService.getStaffMember(teacherLogin) ?: throw RuntimeException()

        teacherInfoView.bind(teacherLogin = teacherLogin, clickable = false)

        teacherPhonesContainerView.removeAllViews()

        teacher.person.contacts.phones.forEach { phone ->
            teacherPhonesContainerView.addView(BrandedPhoneView_.build(this).bind(phone.value))
        }
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
