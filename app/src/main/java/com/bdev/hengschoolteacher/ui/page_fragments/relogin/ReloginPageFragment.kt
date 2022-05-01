package com.bdev.hengschoolteacher.ui.page_fragments.relogin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.lifecycle.ViewModelProvider
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.data.auth.AuthCredentials
import com.bdev.hengschoolteacher.interactors.auth.AuthActionsInteractorImpl
import com.bdev.hengschoolteacher.interactors.user_preferences.UserPreferencesInteractor
import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragment
import com.bdev.hengschoolteacher.ui.views.app.root.HtPageRootView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_relogin.*
import javax.inject.Inject

@AndroidEntryPoint
class ReloginPageFragment : BasePageFragment<ReloginPageFragmentViewModel>() {
    @Inject lateinit var authAsyncService: AuthActionsInteractorImpl
    @Inject lateinit var userPreferencesService: UserPreferencesInteractor

    override fun provideViewModel(): ReloginPageFragmentViewModel =
        ViewModelProvider(this).get(ReloginPageFragmentViewModelImpl::class.java)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.activity_relogin, container, false)

    override fun doOnViewCreated() {
        super.doOnViewCreated()

        doRelogin()

        reloginFailedRestartView.setOnClickListener { doRelogin() }
    }

    private fun doRelogin() {
        val spinnerAnim = AnimationUtils.loadAnimation(context, R.anim.spinner)

        reloginInProgressView.visibility = View.VISIBLE
        reloginInProgressSpinnerView.startAnimation(spinnerAnim)

        reloginFailedView.visibility = View.GONE

        val login = userPreferencesService.getUserLogin()
        val password = userPreferencesService.getUserPassword()

        if (login == null || password == null) {
            onReloginFailure()
        } else {
            authAsyncService.login(AuthCredentials(login, password))
                    .onSuccess { onReloginSuccess() }
                    .onAuthFail { onReloginFailure() }
                    .onOtherFail { onOtherFailure() }
        }
    }

    private fun onReloginSuccess() {
        // todo: go to loading
    }

    private fun onReloginFailure() {
        // todo: go to login
    }

    private fun onOtherFailure() {
//        reloginInProgressSpinnerView.clearAnimation()
//        reloginInProgressView.visibility = View.GONE
//
//        reloginFailedView.visibility = View.VISIBLE
    }
}
