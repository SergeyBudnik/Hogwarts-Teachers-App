package com.bdev.hengschoolteacher.ui.activities.teacher

import android.os.Bundle
import android.os.PersistableBundle
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.interactors.staff_members.StaffMembersStorageInteractor
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import com.bdev.hengschoolteacher.ui.utils.RedirectBuilder
import com.bdev.hengschoolteacher.ui.views.app.AppLayoutView
import com.bdev.hengschoolteacher.ui.views.branded.BrandedPhoneView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_teacher.*
import javax.inject.Inject

@AndroidEntryPoint
class TeacherActivity : BaseActivity() {
    companion object {
        const val EXTRA_TEACHER_LOGIN = "EXTRA_TEACHER_LOGIN"

        fun redirectToChild(current: BaseActivity, teacherLogin: String) {
            RedirectBuilder
                    .redirect(current)
                    .to(TeacherActivity::class.java)
                    .withExtra(EXTRA_TEACHER_LOGIN, teacherLogin)
                    .withAnim(R.anim.slide_open_enter, R.anim.slide_open_exit)
                    .go()
        }
    }

    lateinit var teacherLogin: String

    @Inject lateinit var staffMembersStorageInteractor: StaffMembersStorageInteractor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_teacher)

        teacherLogin = intent.getStringExtra(EXTRA_TEACHER_LOGIN)!!

        teacherHeaderView.setLeftButtonAction { doFinish() }

        val teacher = staffMembersStorageInteractor.getStaffMember(teacherLogin) ?: throw RuntimeException()

        teacherInfoView.bind(teacher = teacher, clickable = false)

        teacherPhonesContainerView.removeAllViews()

        teacher.person.contacts.phones.forEach { phone ->
            teacherPhonesContainerView.addView(BrandedPhoneView(this).bind(personContact = phone))
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
