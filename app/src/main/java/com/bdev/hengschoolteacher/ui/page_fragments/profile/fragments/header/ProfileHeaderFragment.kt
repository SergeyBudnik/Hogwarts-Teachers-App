package com.bdev.hengschoolteacher.ui.page_fragments.profile.fragments.header

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.ui.fragments.common.content_header.CommonContentHeaderFragment
import com.bdev.hengschoolteacher.ui.fragments.common.content_header.views.CommonContentHeaderView
import com.bdev.hengschoolteacher.ui.page_fragments.profile.ProfilePageFragmentViewModel
import com.bdev.hengschoolteacher.ui.page_fragments.profile.ProfilePageFragmentViewModelImpl
import com.bdev.hengschoolteacher.ui.page_fragments.profile.data.ProfilePageFragmentTab
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_profile_header.*

@AndroidEntryPoint
class ProfileHeaderFragment : CommonContentHeaderFragment<
    ProfilePageFragmentTab,
    ProfileHeaderFragmentViewModel
>() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.fragment_profile_header, container, false)

    override fun provideViewModel() =
        ViewModelProvider(this).get(ProfileHeaderFragmentViewModelImpl::class.java)

    override fun providePageViewModel(): ProfilePageFragmentViewModel =
        ViewModelProvider(requireParentFragment()).get(ProfilePageFragmentViewModelImpl::class.java)

    override fun getHeaderView(): CommonContentHeaderView<ProfilePageFragmentTab> = profileHeaderView
}