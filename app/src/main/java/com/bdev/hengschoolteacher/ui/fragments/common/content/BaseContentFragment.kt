package com.bdev.hengschoolteacher.ui.fragments.common.content

import android.view.View
import com.bdev.hengschoolteacher.ui.fragments.BaseFragment
import com.bdev.hengschoolteacher.ui.page_fragments.common.content.BaseContentPageFragmentViewModel
import com.bdev.hengschoolteacher.ui.utils.ViewVisibilityUtils.visibleElseGone
import com.bdev.hengschoolteacher.ui.views.app.header.data.AppHeaderButtonType

abstract class BaseContentFragment<
    TabType,
    ArgsType,
    ViewModelType : BaseContentFragmentViewModel<TabType, ArgsType, *>
> : BaseFragment<ViewModelType>() {
    abstract fun providePageViewModel(): BaseContentPageFragmentViewModel<TabType, ArgsType>

    abstract fun getRootView(): View

    override fun doOnViewCreated() {
        super.doOnViewCreated()

        val pageViewModel = providePageViewModel()

        pageViewModel.getArgsLiveData().observe(this) { args ->
            fragmentViewModel.setArgs(args = args)
        }

        pageViewModel.getTabLiveData().observe(this) { tab ->
            fragmentViewModel.setCurrentTab(tab = tab)
        }

        pageViewModel.getHeaderClickEventLiveData().observe(this) { data ->
            if (fragmentViewModel.isVisible()) {
                data.drainEvents().firstOrNull()?.let { buttonType ->
                    when (buttonType) {
                        AppHeaderButtonType.FIRST -> {
                            fragmentViewModel.onHeaderButton1Clicked()
                        }
                        AppHeaderButtonType.SECOND -> {
                            fragmentViewModel.onHeaderButton2Clicked()
                        }
                        AppHeaderButtonType.THIRD -> {
                            fragmentViewModel.onHeaderButton3Clicked()
                        }
                    }
                }
            }
        }

        fragmentViewModel.getDataLiveData().observe(this) { data ->
            getRootView().visibility = visibleElseGone(visible = data.isVisible())

            if (data.isVisible()) {
                pageViewModel.setHeaderButtons(
                    headerButtons = data.getHeaderButtons()
                )
            }
        }
    }
}