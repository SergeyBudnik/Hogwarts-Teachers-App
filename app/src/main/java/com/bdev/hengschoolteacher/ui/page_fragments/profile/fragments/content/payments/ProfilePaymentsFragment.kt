package com.bdev.hengschoolteacher.ui.page_fragments.profile.fragments.content.payments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.ui.page_fragments.profile.fragments.content.ProfileContentFragment
import com.bdev.hengschoolteacher.ui.utils.ViewVisibilityUtils.visibleElseGone
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_profile_payments.*

@AndroidEntryPoint
class ProfilePaymentsFragment : ProfileContentFragment<ProfilePaymentsPageFragmentViewModel>() {
    override fun provideViewModel(): ProfilePaymentsPageFragmentViewModel =
        ViewModelProvider(this).get(ProfilePaymentsPageFragmentViewModelImpl::class.java)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.fragment_profile_payments, container, false)

    override fun getRootView(): View = profilePaymentsRootView

    override fun doOnViewCreated() {
        super.doOnViewCreated()

        fragmentViewModel.getDataLiveData().observe(this) { data ->
            initList(data = data)
        }
    }

    private fun initList(data: ProfilePaymentsFragmentData) {
        profilePaymentsEmptyView.visibility = visibleElseGone(visible = data.allPayments.isEmpty())
        profilePaymentsEmptyWithFilterView.visibility = visibleElseGone(visible = (data.allPayments.isNotEmpty() && data.filteredPayments.isEmpty()))

        profilePaymentsTeacherPaymentsView.bind(data = data.paymentsViewData)
    }
}
