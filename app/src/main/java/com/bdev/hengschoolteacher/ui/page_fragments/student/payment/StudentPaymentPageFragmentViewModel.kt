package com.bdev.hengschoolteacher.ui.page_fragments.student.payment

import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragmentViewModel
import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragmentViewModelImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

interface StudentPaymentPageFragmentViewModel : BasePageFragmentViewModel {
}

@HiltViewModel
class StudentPaymentPageFragmentViewModelImpl @Inject constructor(
): StudentPaymentPageFragmentViewModel, BasePageFragmentViewModelImpl() {
    override fun goBack() {
        // todo
    }
}