package com.bdev.hengschoolteacher.ui.page_fragments.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.interactors.auth.AuthStorageInteractor
import com.bdev.hengschoolteacher.interactors.profile.ProfileInteractor
import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragment
import com.bdev.hengschoolteacher.ui.views.app.root.HtPageRootView
import com.bdev.hengschoolteacher.ui.views.app.AppMenuView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_settings.*
import javax.inject.Inject

@AndroidEntryPoint
class SettingsPageFragment : BasePageFragment<SettingsPageFragmentViewModel>() {
    @Inject lateinit var authService: AuthStorageInteractor
    @Inject lateinit var profileInteractor: ProfileInteractor

    override fun provideViewModel(): SettingsPageFragmentViewModel =
        ViewModelProvider(this).get(SettingsPageFragmentViewModelImpl::class.java)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.activity_settings, container, false)

    override fun doOnViewCreated() {
        super.doOnViewCreated()

        settingsHeaderView.setLeftButtonAction { settingsMenuLayoutView.openMenu() }

        settingsMenuLayoutView.setCurrentMenuItem(AppMenuView.Item.SETTINGS)

        val me = profileInteractor.getMe()

        settingsAccountNameView.text = me?.person?.name ?: ""
        settingsAccountLoginView.text = me?.login ?: ""

        settingsLogoutView.setOnClickListener { logOut() }
    }

    private fun logOut() {
        authService.clearAuthInfo()

        // todo: go to login
    }
}
