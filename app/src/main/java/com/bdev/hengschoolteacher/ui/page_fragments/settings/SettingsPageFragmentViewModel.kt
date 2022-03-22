package com.bdev.hengschoolteacher.ui.page_fragments.settings

import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragmentViewModel
import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragmentViewModelImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

interface SettingsPageFragmentViewModel : BasePageFragmentViewModel {
}

@HiltViewModel
class SettingsPageFragmentViewModelImpl @Inject constructor(
): SettingsPageFragmentViewModel, BasePageFragmentViewModelImpl() {
    override fun goBack() {
        // todo
    }
}