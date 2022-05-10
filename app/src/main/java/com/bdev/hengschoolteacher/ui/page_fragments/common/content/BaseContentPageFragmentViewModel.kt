package com.bdev.hengschoolteacher.ui.page_fragments.common.content

import androidx.lifecycle.LiveData
import com.bdev.hengschoolteacher.data.common.MutableLiveDataWithState
import com.bdev.hengschoolteacher.ui.events.EventsQueue
import com.bdev.hengschoolteacher.ui.events.EventsQueueLiveDataHolder
import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragmentViewModel
import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragmentViewModelImpl
import com.bdev.hengschoolteacher.ui.views.app.header.data.AppHeaderButtonType
import com.bdev.hengschoolteacher.ui.views.app.header.data.AppHeaderButtons

interface BaseContentPageFragmentViewModel<TabType> : BasePageFragmentViewModel {
    fun getDataLiveData(): LiveData<BaseContentPageFragmentData<TabType>>
    fun getHeaderClickEventLiveData(): LiveData<EventsQueue<AppHeaderButtonType>>

    fun setTab(tab: TabType)
    fun setHeaderButtons(headerButtons: AppHeaderButtons)

    fun notifyHeaderButtonClicked(type: AppHeaderButtonType)
}

abstract class BaseContentPageFragmentViewModelImpl<TabType>(
    private val defaultTab: TabType
): BaseContentPageFragmentViewModel<TabType>, BasePageFragmentViewModelImpl() {
    private val dataLiveData = MutableLiveDataWithState(
        initialValue = getInitialData()
    )

    private val headerClickEventLiveData = EventsQueueLiveDataHolder<AppHeaderButtonType>()

    override fun getDataLiveData() = dataLiveData.getLiveData()

    override fun getHeaderClickEventLiveData() = headerClickEventLiveData.getLiveData()

    override fun setTab(tab: TabType) {
        dataLiveData.updateValue(defaultValue = getInitialData()) { oldValue ->
            oldValue.copy(tab = tab)
        }
    }

    override fun setHeaderButtons(headerButtons: AppHeaderButtons) {
        dataLiveData.updateValue(defaultValue = getInitialData()) { oldValue ->
            oldValue.copy(headerButtons = headerButtons)
        }
    }

    override fun notifyHeaderButtonClicked(type: AppHeaderButtonType) {
        headerClickEventLiveData.postEvent(event = type)
    }

    private fun getInitialData(): BaseContentPageFragmentData<TabType> =
        BaseContentPageFragmentData(
            tab = defaultTab,
            headerButtons = AppHeaderButtons(
                button1 = null,
                button2 = null,
                button3 = null
            )
        )
}