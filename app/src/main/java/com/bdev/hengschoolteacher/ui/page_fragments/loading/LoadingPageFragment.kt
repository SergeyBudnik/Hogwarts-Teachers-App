package com.bdev.hengschoolteacher.ui.page_fragments.loading

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.lifecycle.ViewModelProvider
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragment
import com.bdev.hengschoolteacher.ui.views.app.AppLayoutView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_loading.*

@AndroidEntryPoint
class LoadingPageFragment : BasePageFragment<LoadingPageFragmentViewModel>() {
    override fun provideViewModel(): LoadingPageFragmentViewModel =
        ViewModelProvider(this).get(LoadingPageFragmentViewModelImpl::class.java)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.activity_loading, container, false)

    override fun doOnViewCreated() {
        super.doOnViewCreated()

        fragmentViewModel.getFailureEventsQueueLiveData().observe(this) { eventsQueue ->
            eventsQueue.drainEvents().firstOrNull()?.let {
                onLoadingOtherFailure()
            }
        }

        doLoading()

        loadingFailedRestartView.setOnClickListener { doLoading() }

        loadingFailedContinueView.setOnClickListener {
            fragmentViewModel.proceedWithoutLoading()
        }
    }

    private fun doLoading() {
        val spinnerAnim = AnimationUtils.loadAnimation(context, R.anim.spinner)

        loadingInProgressView.visibility = View.VISIBLE
        loadingInProgressSpinnerView.startAnimation(spinnerAnim)

        loadingFailedView.visibility = View.GONE

        fragmentViewModel.load()
    }

    private fun onLoadingOtherFailure() {
        loadingInProgressSpinnerView.clearAnimation()
        loadingInProgressView.visibility = View.GONE

        loadingFailedView.visibility = View.VISIBLE
    }

    override fun getAppLayoutView(): AppLayoutView? {
        return null
    }
}
