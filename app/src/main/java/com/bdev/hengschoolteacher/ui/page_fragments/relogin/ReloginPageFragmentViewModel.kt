package com.bdev.hengschoolteacher.ui.page_fragments.relogin

import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragmentViewModel
import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragmentViewModelImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

interface ReloginPageFragmentViewModel : BasePageFragmentViewModel {
}

@HiltViewModel
class ReloginPageFragmentViewModelImpl @Inject constructor(
): ReloginPageFragmentViewModel, BasePageFragmentViewModelImpl() {
    override fun goBack() {
        // todo
    }
}