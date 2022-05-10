package com.bdev.hengschoolteacher.ui.page_fragments.profile

import com.bdev.hengschoolteacher.ui.navigation.NavCommand
import com.bdev.hengschoolteacher.ui.page_fragments.common.content.BaseContentPageFragmentViewModel
import com.bdev.hengschoolteacher.ui.page_fragments.common.content.BaseContentPageFragmentViewModelImpl
import com.bdev.hengschoolteacher.ui.page_fragments.profile.data.ProfilePageFragmentTab
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

interface ProfilePageFragmentViewModel : BaseContentPageFragmentViewModel<ProfilePageFragmentTab>

@HiltViewModel
class ProfilePageFragmentViewModelImpl @Inject constructor():
    ProfilePageFragmentViewModel,
    BaseContentPageFragmentViewModelImpl<ProfilePageFragmentTab>(
        defaultTab = ProfilePageFragmentTab.NONE
    )
{
    override fun goBack() {
        navigate(navCommand = NavCommand.quit())
    }
}