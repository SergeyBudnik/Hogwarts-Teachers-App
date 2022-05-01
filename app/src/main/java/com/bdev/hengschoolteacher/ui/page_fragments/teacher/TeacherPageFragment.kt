package com.bdev.hengschoolteacher.ui.page_fragments.teacher

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.interactors.staff_members.StaffMembersStorageInteractor
import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragment
import com.bdev.hengschoolteacher.ui.views.app.root.HtPageRootView
import com.bdev.hengschoolteacher.ui.views.branded.BrandedPhoneView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_teacher.*
import javax.inject.Inject

@AndroidEntryPoint
class TeacherPageFragment : BasePageFragment<TeacherPageFragmentViewModel>() {
    companion object {
        const val EXTRA_TEACHER_LOGIN = "EXTRA_TEACHER_LOGIN"
    }

    lateinit var teacherLogin: String

    @Inject lateinit var staffMembersStorageInteractor: StaffMembersStorageInteractor

    override fun provideViewModel(): TeacherPageFragmentViewModel =
        ViewModelProvider(this).get(TeacherPageFragmentViewModelImpl::class.java)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.activity_teacher, container, false)

    override fun doOnViewCreated() {
        super.doOnViewCreated()

        // teacherLogin = intent.getStringExtra(EXTRA_TEACHER_LOGIN)!! todo

        teacherHeaderView.setLeftButtonAction { doFinish() }

        val teacher = staffMembersStorageInteractor.getStaffMember(teacherLogin) ?: throw RuntimeException()

        teacherInfoView.bind(teacher = teacher, clickable = false)

        teacherPhonesContainerView.removeAllViews()

        teacher.person.contacts.phones.forEach { phone ->
            teacherPhonesContainerView.addView(BrandedPhoneView(requireContext()).bind(personContact = phone))
        }
    }

    private fun doFinish() {
//        finish()
//        overridePendingTransition(R.anim.slide_close_enter, R.anim.slide_close_exit)
    }
}
