package com.bdev.hengschoolteacher.ui.page_fragments.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.ui.fragments.app_menu.AppMenuFragment
import com.bdev.hengschoolteacher.ui.fragments.app_menu.data.AppMenuItem
import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_settings.*

@AndroidEntryPoint
class SettingsPageFragment : BasePageFragment<SettingsPageFragmentViewModel>() {
    override fun provideViewModel(): SettingsPageFragmentViewModel =
        ViewModelProvider(this).get(SettingsPageFragmentViewModelImpl::class.java)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.activity_settings, container, false)

    override fun doOnViewCreated() {
        super.doOnViewCreated()

        initHeader()
        initMenu()
        initLogoutButton()

        fragmentViewModel.getDataLiveData().observe(this) { data ->
            updateView(data = data)
        }
    }

    private fun initHeader() {
        settingsHeaderView.setLeftButtonAction { settingsMenuLayoutView.openMenu() }
    }

    private fun initMenu() {
        getAppMenuFragment().setCurrentItem(item = AppMenuItem.SETTINGS)
    }

    private fun initLogoutButton() {
        settingsLogoutView.setOnClickListener {
            fragmentViewModel.logout()
        }
    }

    private fun updateView(data: SettingsPageFragmentData) {
        settingsAccountNameView.text = data.name
        settingsAccountLoginView.text = data.login
    }

    private fun getAppMenuFragment(): AppMenuFragment =
        childFragmentManager.findFragmentById(R.id.appMenuFragment) as AppMenuFragment
}
