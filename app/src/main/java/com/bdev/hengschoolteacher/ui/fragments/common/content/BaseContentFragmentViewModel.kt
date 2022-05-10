package com.bdev.hengschoolteacher.ui.fragments.common.content

import androidx.lifecycle.LiveData
import com.bdev.hengschoolteacher.ui.fragments.BaseFragmentViewModel
import com.bdev.hengschoolteacher.ui.fragments.BaseFragmentViewModelImpl

interface BaseContentFragmentViewModel<TabType, DataType : BaseContentFragmentData> : BaseFragmentViewModel {
    fun getDataLiveData(): LiveData<DataType>

    fun isVisible(): Boolean

    fun setCurrentTab(tab: TabType)

    fun onHeaderButton1Clicked()
    fun onHeaderButton2Clicked()
    fun onHeaderButton3Clicked()
}

abstract class BaseContentFragmentViewModelImpl<TabType, DataType : BaseContentFragmentData> :
    BaseContentFragmentViewModel<TabType, DataType>,
    BaseFragmentViewModelImpl()
{
    override fun onHeaderButton1Clicked() {}
    override fun onHeaderButton2Clicked() {}
    override fun onHeaderButton3Clicked() {}
}