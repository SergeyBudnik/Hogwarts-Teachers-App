package com.bdev.hengschoolteacher.ui.activities

import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.view.animation.AnimationUtils
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.interactors.groups.GroupsLoadingInteractor
import com.bdev.hengschoolteacher.interactors.lessons_status.LessonsStatusLoadingInteractor
import com.bdev.hengschoolteacher.interactors.staff_members.StaffMembersLoadingInteractor
import com.bdev.hengschoolteacher.interactors.students.StudentsLoadingInteractor
import com.bdev.hengschoolteacher.interactors.students_attendances.StudentsAttendancesLoadingInteractor
import com.bdev.hengschoolteacher.interactors.students_payments.StudentsPaymentsLoadingInteractor
import com.bdev.hengschoolteacher.ui.activities.profile.ProfileLessonsActivity
import com.bdev.hengschoolteacher.ui.utils.RedirectBuilder
import com.bdev.hengschoolteacher.ui.views.app.AppLayoutView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_loading.*
import javax.inject.Inject

@AndroidEntryPoint
class LoadingActivity : BaseActivity() {
    companion object {
        fun redirectToSibling(current: BaseActivity) {
            RedirectBuilder
                    .redirect(current)
                    .to(LoadingActivity::class.java)
                    .goAndCloseAll()
        }
    }

    @Inject lateinit var studentsLoadingInteractor: StudentsLoadingInteractor
    @Inject lateinit var studentsAttendancesLoadingInteractor: StudentsAttendancesLoadingInteractor
    @Inject lateinit var staffMembersLoadingInteractor: StaffMembersLoadingInteractor
    @Inject lateinit var lessonsStatusLoadingInteractor: LessonsStatusLoadingInteractor
    @Inject lateinit var studentsPaymentsLoadingInteractor: StudentsPaymentsLoadingInteractor
    @Inject lateinit var groupsLoadingInteractor: GroupsLoadingInteractor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_loading)

        doLoading()

        loadingFailedRestartView.setOnClickListener { doLoading() }
        loadingFailedContinueView.setOnClickListener { doRedirect() }
    }

    private fun doLoading() {
        val spinnerAnim = AnimationUtils.loadAnimation(this, R.anim.spinner)

        loadingInProgressView.visibility = View.VISIBLE
        loadingInProgressSpinnerView.startAnimation(spinnerAnim)

        loadingFailedView.visibility = View.GONE

        val loadStudentsPromise = studentsLoadingInteractor.load()
        val loadLessonsStatusPromise = lessonsStatusLoadingInteractor.load()
        val loadGroupsPromise = groupsLoadingInteractor.load()
        val loadStudentsAttendancesPromise = studentsAttendancesLoadingInteractor.load()
        val loadStudentsPaymentsPromise = studentsPaymentsLoadingInteractor.load()
        val loadStaffMembersPromise = staffMembersLoadingInteractor.load()

        loadLessonsStatusPromise
                .and(loadStudentsPromise)
                .and(loadGroupsPromise)
                .and(loadStudentsAttendancesPromise)
                .and(loadStudentsPaymentsPromise)
                .and(loadStaffMembersPromise)
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

    override fun getAppLayoutView(): AppLayoutView? {
        return null
    }
}
