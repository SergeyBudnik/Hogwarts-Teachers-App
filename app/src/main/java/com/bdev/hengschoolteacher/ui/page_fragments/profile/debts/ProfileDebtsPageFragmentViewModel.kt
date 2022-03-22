package com.bdev.hengschoolteacher.ui.page_fragments.profile.debts

import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragmentViewModel
import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragmentViewModelImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

interface ProfileDebtsPageFragmentViewModel : BasePageFragmentViewModel {
}

@HiltViewModel
class ProfileDebtsPageFragmentViewModelImpl @Inject constructor(
): ProfileDebtsPageFragmentViewModel, BasePageFragmentViewModelImpl() {
    override fun goBack() {
        // todo
    }
}