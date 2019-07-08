package com.bdev.hengschoolteacher.ui.activities.profile

import android.annotation.SuppressLint
import android.view.View
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.service.profile.ProfileService
import com.bdev.hengschoolteacher.service.teacher.TeacherPaymentsService
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import com.bdev.hengschoolteacher.ui.utils.RedirectBuilder
import kotlinx.android.synthetic.main.activity_profile_payments.*
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EActivity

@SuppressLint("Registered")
@EActivity(R.layout.activity_profile_payments)
open class ProfilePaymentsActivity : BaseActivity() {
    companion object {
        fun redirectToSibling(current: BaseActivity) {
            RedirectBuilder
                    .redirect(current)
                    .to(ProfilePaymentsActivity_::class.java)
                    .goAndCloseCurrent()
        }
    }

    @Bean
    lateinit var profileService: ProfileService
    @Bean
    lateinit var teacherPaymentsService: TeacherPaymentsService

    private var filterEnabled: Boolean = true

    @AfterViews
    fun init() {
        profilePaymentsHeaderView
                .setLeftButtonAction { profilePaymentsMenuLayoutView.openMenu() }
                .setFirstRightButtonAction { toggleFilter() }
                .setFirstRightButtonActive(filterEnabled)

        profilePaymentsEmptyWithFilterView.bind {
            toggleFilter()
        }

        initList()
    }

    private fun toggleFilter() {
        filterEnabled = !filterEnabled

        profilePaymentsHeaderView.setFirstRightButtonActive(filterEnabled)

        initList()
    }

    private fun initList() {
        val teacherId = profileService.getMe()?.id

        val allPayments = teacherId?.let {
            teacherPaymentsService.getPayments(
                    teacherId = teacherId,
                    onlyUnprocessed = false
            )
        } ?: emptyList()

        val filteredPayments = allPayments.filter { payment -> !filterEnabled || !payment.processed }

        profilePaymentsEmptyView.visibility =
                if (allPayments.isEmpty()) {
                    View.VISIBLE
                } else {
                    View.GONE
                }

        profilePaymentsEmptyWithFilterView.visibility =
                if (allPayments.isNotEmpty() && filteredPayments.isEmpty()) {
                    View.VISIBLE
                } else {
                    View.GONE
                }

        profilePaymentsTeacherPaymentsView.bind(
                payments = filteredPayments,
                singleTeacher = true,
                editable = false
        )
    }
}
