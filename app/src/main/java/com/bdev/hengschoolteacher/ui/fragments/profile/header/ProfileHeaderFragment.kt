package com.bdev.hengschoolteacher.ui.fragments.profile.header

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.ui.fragments.BaseFragment
import com.bdev.hengschoolteacher.ui.fragments.profile.header.data.ProfileHeaderFragmentItem
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_profile_header.*

@AndroidEntryPoint
class ProfileHeaderFragment : BaseFragment<ProfileHeaderFragmentViewModel>() {
    override fun provideViewModel() =
        ViewModelProvider(this).get(ProfileHeaderFragmentViewModelImpl::class.java)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.fragment_profile_header, container, false)

    override fun doOnViewCreated() {
        super.doOnViewCreated()

        fragmentViewModel.getDataLiveData().observe(this) { data ->
            updateView(data = data)
        }
    }

    fun setCurrentItem(item: ProfileHeaderFragmentItem) {
        fragmentViewModel.setCurrentItem(item = item)
    }

    private fun updateView(data: ProfileHeaderFragmentData) {
        profileHeaderView.bind(
            data = data,
            navCommandHandler = { navCommand ->
                fragmentViewModel.navigate(navCommand = navCommand)
            }
        )
    }
}