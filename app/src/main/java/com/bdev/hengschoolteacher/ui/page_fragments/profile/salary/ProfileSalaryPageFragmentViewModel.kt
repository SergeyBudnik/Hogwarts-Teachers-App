package com.bdev.hengschoolteacher.ui.page_fragments.profile.salary

import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragmentViewModel
import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragmentViewModelImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

interface ProfileSalaryPageFragmentViewModel : BasePageFragmentViewModel {
}

@HiltViewModel
class ProfileSalaryPageFragmentViewModelImpl @Inject constructor(
): ProfileSalaryPageFragmentViewModel, BasePageFragmentViewModelImpl() {
    override fun goBack() {
        // todo
    }
}