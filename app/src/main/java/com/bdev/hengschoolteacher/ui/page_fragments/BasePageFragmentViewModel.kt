package com.bdev.hengschoolteacher.ui.page_fragments

import com.bdev.hengschoolteacher.ui.fragments.BaseFragmentViewModel
import com.bdev.hengschoolteacher.ui.fragments.BaseFragmentViewModelImpl

interface BasePageFragmentViewModel : BaseFragmentViewModel {
    fun goBack()
}

abstract class BasePageFragmentViewModelImpl : BasePageFragmentViewModel, BaseFragmentViewModelImpl() {

}