package com.bdev.hengschoolteacher.ui.page_fragments.teacher

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragment
import com.bdev.hengschoolteacher.ui.views.branded.BrandedPhoneView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.page_fragment_teacher.*

@AndroidEntryPoint
class TeacherPageFragment : BasePageFragment<TeacherPageFragmentViewModel>() {
    private val args: TeacherPageFragmentArgs by navArgs()

    override fun provideViewModel(): TeacherPageFragmentViewModel =
        ViewModelProvider(this).get(TeacherPageFragmentViewModelImpl::class.java)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.page_fragment_teacher, container, false)

    override fun doOnViewCreated() {
        super.doOnViewCreated()

        initHeader()

        fragmentViewModel.getDataLiveData().observe(this) { data ->
            updateView(data = data)
        }

        fragmentViewModel.init(teacherLogin = args.args.login)
    }

    private fun initHeader() {
        teacherHeaderView.setLeftButtonAction {
            fragmentViewModel.goBack()
        }
    }

    private fun updateView(data: TeacherPageFragmentData) {
        teacherInfoView.bind(teacher = data.teacher, clickable = false)

        teacherPhonesContainerView.removeAllViews()

        data.teacher.person.contacts.phones.forEach { phone ->
            teacherPhonesContainerView.addView(
                BrandedPhoneView(requireContext()).bind(personContact = phone)
            )
        }
    }
}
