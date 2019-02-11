package com.bdev.hengschoolteacher.ui.activities

import android.annotation.SuppressLint
import android.view.View
import android.view.animation.Animation
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.async.SchoolDataAsyncService
import com.bdev.hengschoolteacher.ui.activities.profile.ProfileLessonsActivity_
import com.bdev.hengschoolteacher.ui.utils.RedirectUtils.Companion.redirect
import kotlinx.android.synthetic.main.activity_loading.*
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EActivity
import org.androidannotations.annotations.res.AnimationRes

@SuppressLint("Registered")
@EActivity(R.layout.activity_loading)
open class LoadingActivity : BaseActivity() {
    @Bean
    lateinit var schoolDataAsyncService: SchoolDataAsyncService

    @AnimationRes(R.anim.spinner)
    lateinit var spinnerAnim: Animation

    @AfterViews
    fun init() {
        doLoading()

        loadingFailedRestartView.setOnClickListener { doLoading() }
    }

    private fun doLoading() {
        loadingInProgressView.visibility = View.VISIBLE
        loadingInProgressSpinnerView.startAnimation(spinnerAnim)

        loadingFailedView.visibility = View.GONE

        schoolDataAsyncService.load()
                .onSuccess { runOnUiThread { onLoadingSuccess() } }
                .onAuthFail { runOnUiThread { onLoadingAuthFailure() } }
                .onOtherFail { runOnUiThread { onLoadingOtherFailure() } }
    }

    private fun onLoadingSuccess() {
        redirect(this)
                .to(ProfileLessonsActivity_::class.java)
                .goAndCloseCurrent()
    }

    private fun onLoadingAuthFailure() {
        redirect(this)
                .to(ReloginActivity_::class.java)
                .goAndCloseCurrent()
    }

    private fun onLoadingOtherFailure() {
        loadingInProgressSpinnerView.clearAnimation()
        loadingInProgressView.visibility = View.GONE

        loadingFailedView.visibility = View.VISIBLE
    }
}
