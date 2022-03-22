package com.bdev.hengschoolteacher.ui.page_fragments.start

import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragmentViewModel
import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragmentViewModelImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

interface StartPageFragmentViewModel : BasePageFragmentViewModel {
}

@HiltViewModel
class StartPageFragmentViewModelImpl @Inject constructor(
): StartPageFragmentViewModel, BasePageFragmentViewModelImpl() {
    override fun goBack() {
        // todo
    }
}