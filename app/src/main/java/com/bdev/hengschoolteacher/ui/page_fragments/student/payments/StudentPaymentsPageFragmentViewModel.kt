package com.bdev.hengschoolteacher.ui.page_fragments.student.payments

import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragmentViewModel
import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragmentViewModelImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

interface StudentPaymentsPageFragmentViewModel : BasePageFragmentViewModel {
}

@HiltViewModel
class StudentPaymentsPageFragmentViewModelImpl @Inject constructor(
): StudentPaymentsPageFragmentViewModel, BasePageFragmentViewModelImpl() {
    override fun goBack() {
        // todo
    }
}