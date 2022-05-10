package com.bdev.hengschoolteacher.ui.page_fragments.common.content

import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.ui.fragments.app_menu.AppMenuFragment
import com.bdev.hengschoolteacher.ui.fragments.app_menu.data.AppMenuItem
import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragment
import com.bdev.hengschoolteacher.ui.views.app.AppHeaderIconHandler
import com.bdev.hengschoolteacher.ui.views.app.AppHeaderView
import com.bdev.hengschoolteacher.ui.views.app.header.data.AppHeaderButton
import com.bdev.hengschoolteacher.ui.views.app.header.data.AppHeaderButtonType
import com.bdev.hengschoolteacher.ui.views.app.root.HtPageRootView

abstract class BaseContentPageFragment<ViewModelType : BaseContentPageFragmentViewModel<*>>(
    private val menuItem: AppMenuItem
) : BasePageFragment<ViewModelType>() {
    abstract fun getRootView(): HtPageRootView
    abstract fun getPrimaryHeaderView(): AppHeaderView

    override fun doOnViewCreated() {
        super.doOnViewCreated()

        fragmentViewModel.getDataLiveData().observe(this) { data ->
            updateHeaderView(data = data)
        }

        getMenu().setCurrentItem(item = menuItem)
    }

    private fun updateHeaderView(data: BaseContentPageFragmentData<*>) {
        getPrimaryHeaderView().setLeftButtonAction {
            getRootView().openMenu()
        }

        updateHeaderButtonView(
            viewHandler = getPrimaryHeaderView().getFirstButtonHandler(),
            data = data.headerButtons.button1,
            type = AppHeaderButtonType.FIRST
        )

        updateHeaderButtonView(
            viewHandler = getPrimaryHeaderView().getSecondButtonHandler(),
            data = data.headerButtons.button2,
            type = AppHeaderButtonType.SECOND
        )

        updateHeaderButtonView(
            viewHandler = getPrimaryHeaderView().getThirdButtonHandler(),
            data = data.headerButtons.button3,
            type = AppHeaderButtonType.THIRD
        )
    }

    private fun updateHeaderButtonView(
        viewHandler: AppHeaderIconHandler,
        data: AppHeaderButton?,
        type: AppHeaderButtonType
    ) {
        viewHandler.setVisible(visible = data != null)

        if (data != null) {
            viewHandler
                .setIcon(iconId = data.iconId)
                .setToggled(toggled = data.toggled)
                .setAction { fragmentViewModel.notifyHeaderButtonClicked(type = type) }
        }
    }

    private fun getMenu(): AppMenuFragment =
        childFragmentManager.findFragmentById(R.id.appMenuFragment) as AppMenuFragment
}