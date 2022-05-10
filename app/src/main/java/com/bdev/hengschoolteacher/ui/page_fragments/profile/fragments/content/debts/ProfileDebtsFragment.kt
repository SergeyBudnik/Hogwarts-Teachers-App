package com.bdev.hengschoolteacher.ui.page_fragments.profile.fragments.content.debts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.ui.page_fragments.profile.fragments.content.ProfileContentFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_profile_debts.*

@AndroidEntryPoint
class ProfileDebtsFragment : ProfileContentFragment<ProfileDebtsFragmentViewModel>() {
    override fun provideViewModel() =
        ViewModelProvider(this).get(ProfileDebtsFragmentViewModelImpl::class.java)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.fragment_profile_debts, container, false)

    override fun getRootView(): View = profileDebtsRootView

    override fun doOnViewCreated() {
        super.doOnViewCreated()

        fragmentViewModel.getDataLiveData().observe(this) { data ->
            profileDebtsListView.bind(
                studentsToExpectedDebt = data.studentsToExpectedDebt,
                searchQuery = "",
                withDebtsOnly = true
            )
        }
    }
}