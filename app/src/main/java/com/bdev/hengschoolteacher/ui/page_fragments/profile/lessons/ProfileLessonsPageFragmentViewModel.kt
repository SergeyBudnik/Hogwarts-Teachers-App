package com.bdev.hengschoolteacher.ui.page_fragments.profile.lessons

import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragmentViewModel
import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragmentViewModelImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

interface ProfileLessonsPageFragmentViewModel : BasePageFragmentViewModel {
}

@HiltViewModel
class ProfileLessonsPageFragmentViewModelImpl @Inject constructor(
): ProfileLessonsPageFragmentViewModel, BasePageFragmentViewModelImpl() {
    override fun goBack() {
        TODO("Not yet implemented")
    }
}