package com.bdev.hengschoolteacher.ui.page_fragments.profile.fragments.content

import com.bdev.hengschoolteacher.ui.fragments.common.content.BaseContentFragmentData
import com.bdev.hengschoolteacher.ui.fragments.common.content.BaseContentFragmentViewModel
import com.bdev.hengschoolteacher.ui.fragments.common.content.BaseContentFragmentViewModelImpl
import com.bdev.hengschoolteacher.ui.page_fragments.profile.data.ProfilePageFragmentTab

interface ProfileContentFragmentViewModel<DataType : BaseContentFragmentData> :
    BaseContentFragmentViewModel<ProfilePageFragmentTab, Any, DataType>

abstract class ProfileContentFragmentViewModelImpl<DataType : BaseContentFragmentData> :
    BaseContentFragmentViewModelImpl<ProfilePageFragmentTab, Any, DataType>()
{
    final override fun setArgs(args: Any) { /* Do nothing */ }
}