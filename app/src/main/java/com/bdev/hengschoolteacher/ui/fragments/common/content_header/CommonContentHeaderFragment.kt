package com.bdev.hengschoolteacher.ui.fragments.common.content_header

import com.bdev.hengschoolteacher.ui.fragments.BaseFragment
import com.bdev.hengschoolteacher.ui.fragments.common.content_header.views.CommonContentHeaderView
import com.bdev.hengschoolteacher.ui.page_fragments.common.content.BaseContentPageFragmentViewModel

abstract class CommonContentHeaderFragment<
    Tab,
    ViewModel : CommonContentHeaderFragmentViewModel<Tab>
>: BaseFragment<ViewModel>() {
    abstract fun providePageViewModel(): BaseContentPageFragmentViewModel<Tab, *>

    abstract fun getHeaderView(): CommonContentHeaderView<Tab>

    override fun doOnViewCreated() {
        super.doOnViewCreated()

        providePageViewModel().getTabLiveData().observe(this) { tab ->
            fragmentViewModel.setCurrentItem(tab = tab)
        }

        fragmentViewModel.getDataLiveData().observe(this) { data ->
            updateView(data = data)
        }
    }

    private fun updateView(data: CommonContentHeaderFragmentData<Tab>) {
        getHeaderView().bind(
            data = data,
            tabClickedAction = { tab ->
                providePageViewModel().setTab(tab = tab)
            }
        )
    }
}