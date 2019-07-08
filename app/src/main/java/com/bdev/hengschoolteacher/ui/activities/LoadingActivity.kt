package com.bdev.hengschoolteacher.ui.activities

import android.annotation.SuppressLint
import android.view.View
import android.view.animation.Animation
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.async.SchoolDataAsyncService
import com.bdev.hengschoolteacher.ui.activities.profile.ProfileLessonsActivity
import com.bdev.hengschoolteacher.ui.utils.RedirectBuilder
import kotlinx.android.synthetic.main.activity_loading.*
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EActivity
import org.androidannotations.annotations.res.AnimationRes

@SuppressLint("Registered")
@EActivity(R.layout.activity_loading)
open class LoadingActivity : BaseActivity() {
    companion object {
        fun redirectToSibling(current: BaseActivity) {
            RedirectBuilder
                    .redirect(current)
                    .to(LoadingActivity_::class.java)
                    .goAndCloseCurrent()
        }
    }

    @Bean
    lateinit var schoolDataAsyncService: SchoolDataAsyncService

    @AnimationRes(R.anim.spinner)
    lateinit var spinnerAnim: Animation

    @AfterViews
    fun init() {
        doLoading()

        loadingFailedRestartView.setOnClickListener { doLoading() }
        loadingFailedContinueView.setOnClickListener { doRedirect() }
    }

    private fun doLoading() {
        loadingInProgressView.visibility = View.VISIBLE
        loadingInProgressSpinnerView.startAnimation(spinnerAnim)

        loadingFailedView.visibility = View.GONE

        val loadPromise = schoolDataAsyncService.load()
        val loadStudentsPromise = schoolDataAsyncService.loadStudents()
        val loadGroupsPromise = schoolDataAsyncService.loadGroups()
        val loadTeachersPromise = schoolDataAsyncService.loadTeachers()
        val loadStudentsAttendancesPromise = schoolDataAsyncService.loadStudentsAttendances()
        val loadStudentsPaymentsPromise = schoolDataAsyncService.loadStudentsPayments()
        val loadLessonsTransferPromise = schoolDataAsyncService.loadLessonsTransfers()

        loadPromise
                .and(loadStudentsPromise)
                .and(loadGroupsPromise)
                .and(loadTeachersPromise)
                .and(loadStudentsAttendancesPromise)
                .and(loadStudentsPaymentsPromise)
                .and(loadLessonsTransferPromise)
                .onSuccess { runOnUiThread { doRedirect() } }
                .onAuthFail { runOnUiThread { onLoadingAuthFailure() } }
                .onOtherFail { runOnUiThread { onLoadingOtherFailure() } }
    }

    private fun doRedirect() {
        ProfileLessonsActivity.redirectToSibling(this)
    }

    private fun onLoadingAuthFailure() {
        ReloginActivity.redirectToSibling(this)
    }

    private fun onLoadingOtherFailure() {
        loadingInProgressSpinnerView.clearAnimation()
        loadingInProgressView.visibility = View.GONE

        loadingFailedView.visibility = View.VISIBLE
    }
}
