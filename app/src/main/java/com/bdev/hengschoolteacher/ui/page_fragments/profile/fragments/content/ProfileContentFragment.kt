package com.bdev.hengschoolteacher.ui.page_fragments.profile.fragments.content

import androidx.lifecycle.ViewModelProvider
import com.bdev.hengschoolteacher.ui.fragments.common.content.BaseContentFragment
import com.bdev.hengschoolteacher.ui.page_fragments.profile.ProfilePageFragmentViewModel
import com.bdev.hengschoolteacher.ui.page_fragments.profile.ProfilePageFragmentViewModelImpl
import com.bdev.hengschoolteacher.ui.page_fragments.profile.data.ProfilePageFragmentTab

abstract class ProfileContentFragment<
    ViewModelType : ProfileContentFragmentViewModel<*>
> : BaseContentFragment<
    ProfilePageFragmentTab,
    Any,
    ViewModelType
>() {
    final override fun providePageViewModel() =
        ViewModelProvider(requireParentFragment()).get(ProfilePageFragmentViewModelImpl::class.java)
}