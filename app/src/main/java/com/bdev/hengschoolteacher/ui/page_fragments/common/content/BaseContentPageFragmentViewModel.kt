package com.bdev.hengschoolteacher.ui.page_fragments.common.content

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bdev.hengschoolteacher.ui.events.EventsQueue
import com.bdev.hengschoolteacher.ui.events.EventsQueueLiveDataHolder
import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragmentViewModel
import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragmentViewModelImpl
import com.bdev.hengschoolteacher.ui.views.app.header.data.AppHeaderButtonType
import com.bdev.hengschoolteacher.ui.views.app.header.data.AppHeaderButtons

interface BaseContentPageFragmentViewModel<TabType, ArgsType> : BasePageFragmentViewModel {
    fun getArgsLiveData(): LiveData<ArgsType>
    fun getTabLiveData(): LiveData<TabType>
    fun getHeaderButtonsLiveData(): LiveData<AppHeaderButtons>

    fun getHeaderClickEventLiveData(): LiveData<EventsQueue<AppHeaderButtonType>>

    fun init(args: ArgsType)

    fun setTab(tab: TabType)
    fun setHeaderButtons(headerButtons: AppHeaderButtons)

    fun notifyHeaderButtonClicked(type: AppHeaderButtonType)
}

abstract class BaseContentPageFragmentViewModelImpl<TabType : Any, ArgsType : Any>(
    defaultTab: TabType
): BaseContentPageFragmentViewModel<TabType, ArgsType>, BasePageFragmentViewModelImpl() {
    private val argsLiveData = MutableLiveData<ArgsType>()
    private val tabLiveData = MutableLiveData(defaultTab)
    private val headerButtonsLiveData = MutableLiveData<AppHeaderButtons>()

    private val headerClickEventLiveData = EventsQueueLiveDataHolder<AppHeaderButtonType>()

    override fun getArgsLiveData() = argsLiveData
    override fun getTabLiveData() = tabLiveData
    override fun getHeaderButtonsLiveData() = headerButtonsLiveData

    override fun getHeaderClickEventLiveData() = headerClickEventLiveData.getLiveData()

    override fun init(args: ArgsType) {
        argsLiveData.value = args
    }

    override fun setTab(tab: TabType) {
        tabLiveData.value = tab
    }

    override fun setHeaderButtons(headerButtons: AppHeaderButtons) {
        headerButtonsLiveData.value = headerButtons
    }

    override fun notifyHeaderButtonClicked(type: AppHeaderButtonType) {
        headerClickEventLiveData.postEvent(event = type)
    }
}