package com.bdev.hengschoolteacher.ui.page_fragments.start

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.interactors.auth.AuthStorageInteractor
import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragment
import com.bdev.hengschoolteacher.ui.page_fragments.loading.LoadingPageFragment
import com.bdev.hengschoolteacher.ui.page_fragments.login.LoginPageFragment
import com.bdev.hengschoolteacher.ui.page_fragments.settings.SettingsPageFragmentViewModel
import com.bdev.hengschoolteacher.ui.page_fragments.settings.SettingsPageFragmentViewModelImpl
import com.bdev.hengschoolteacher.ui.views.app.AppLayoutView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class StartPageFragment : BasePageFragment<StartPageFragmentViewModel>() {
    @Inject lateinit var authService: AuthStorageInteractor

    override fun provideViewModel(): StartPageFragmentViewModel =
        ViewModelProvider(this).get(StartPageFragmentViewModelImpl::class.java)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.activity_loading, container, false) // todo: activity_start

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val authInfo = authService.getAuthInfo()

        // appUpdateService.enqueueUpdate()

        if (authInfo == null) {
            // LoginPageFragment.redirectToSibling(this) todo: login
        } else {
            // LoadingPageFragment.redirectToSibling(this) todo: loading
        }
    }

    override fun getAppLayoutView(): AppLayoutView? {
        return null
    }
}
