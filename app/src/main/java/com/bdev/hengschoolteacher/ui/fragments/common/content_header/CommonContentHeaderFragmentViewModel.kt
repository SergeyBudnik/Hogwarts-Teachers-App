package com.bdev.hengschoolteacher.ui.fragments.common.content_header

import androidx.lifecycle.LiveData
import com.bdev.hengschoolteacher.data.common.MutableLiveDataWithState
import com.bdev.hengschoolteacher.data.common.NullableMutableLiveDataWithState
import com.bdev.hengschoolteacher.ui.fragments.BaseFragmentViewModel
import com.bdev.hengschoolteacher.ui.fragments.BaseFragmentViewModelImpl

interface CommonContentHeaderFragmentViewModel<TabType> : BaseFragmentViewModel {
    fun getDataLiveData(): LiveData<CommonContentHeaderFragmentData<TabType>>

    fun setCurrentItem(tab: TabType)
}

abstract class CommonContentHeaderFragmentViewModelImpl<TabType>(
    protected val initialData: CommonContentHeaderFragmentData<TabType>
): CommonContentHeaderFragmentViewModel<TabType>, BaseFragmentViewModelImpl() {
    protected val dataLiveData = MutableLiveDataWithState(initialValue = initialData)

    override fun getDataLiveData() = dataLiveData.getLiveData()

    override fun setCurrentItem(tab: TabType) {
        dataLiveData.setValue(mutator = { oldValue ->
            oldValue.copy(tab = tab)
        })
    }
}