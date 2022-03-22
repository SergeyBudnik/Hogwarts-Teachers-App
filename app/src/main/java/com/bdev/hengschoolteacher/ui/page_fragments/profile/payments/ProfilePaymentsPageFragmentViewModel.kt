package com.bdev.hengschoolteacher.ui.page_fragments.profile.payments

import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragmentViewModel
import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragmentViewModelImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

interface ProfilePaymentsPageFragmentViewModel : BasePageFragmentViewModel {
}

@HiltViewModel
class ProfilePaymentsPageFragmentViewModelImpl @Inject constructor(
): ProfilePaymentsPageFragmentViewModel, BasePageFragmentViewModelImpl() {
    override fun goBack() {
        // todo
    }
}