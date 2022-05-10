package com.bdev.hengschoolteacher.ui.page_fragments.profile.fragments.content.salary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.ui.page_fragments.profile.fragments.content.ProfileContentFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_profile_salary.*

@AndroidEntryPoint
class ProfileSalaryFragment : ProfileContentFragment<ProfileSalaryFragmentViewModel>() {
    override fun provideViewModel(): ProfileSalaryFragmentViewModel =
        ViewModelProvider(this).get(ProfileSalaryFragmentViewModelImpl::class.java)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.fragment_profile_salary, container, false)

    override fun getRootView() = profileSalaryRootView

    override fun doOnViewCreated() {
        super.doOnViewCreated()

        fragmentViewModel.getDataLiveData().observe(this) { data ->
            profileSalaryListView.init(
                teacher = data.me,
                teacherPayments = data.payments
            )
        }
    }
}