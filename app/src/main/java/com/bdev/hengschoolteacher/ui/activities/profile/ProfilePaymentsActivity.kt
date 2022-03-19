package com.bdev.hengschoolteacher.ui.activities.profile

import android.annotation.SuppressLint
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.interactors.alerts.profile.AlertsProfileInteractorImpl
import com.bdev.hengschoolteacher.interactors.profile.ProfileServiceImpl
import com.bdev.hengschoolteacher.interactors.staff.StaffMembersStorageServiceImpl
import com.bdev.hengschoolteacher.interactors.students.StudentsStorageInteractor
import com.bdev.hengschoolteacher.interactors.students.StudentsStorageInteractorImpl
import com.bdev.hengschoolteacher.interactors.students_payments.StudentsPaymentsProviderService
import com.bdev.hengschoolteacher.interactors.students_payments.StudentsPaymentsProviderServiceImpl
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import com.bdev.hengschoolteacher.ui.utils.RedirectBuilder
import com.bdev.hengschoolteacher.ui.utils.ViewVisibilityUtils.visibleElseGone
import com.bdev.hengschoolteacher.ui.views.app.AppLayoutView
import com.bdev.hengschoolteacher.ui.views.app.AppMenuView
import com.bdev.hengschoolteacher.ui.views.app.payments.PaymentsItemViewData
import com.bdev.hengschoolteacher.ui.views.app.payments.PaymentsViewData
import com.bdev.hengschoolteacher.ui.views.app.profile.ProfileHeaderView
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
    lateinit var profileService: ProfileServiceImpl
    @Bean(StudentsPaymentsProviderServiceImpl::class)
    lateinit var studentsPaymentsProviderService: StudentsPaymentsProviderService
    @Bean
    lateinit var alertsProfileService: AlertsProfileInteractorImpl
    @Bean
    lateinit var staffMembersStorageService: StaffMembersStorageServiceImpl
    @Bean(StudentsStorageInteractorImpl::class)
    lateinit var studentsStorageInteractor: StudentsStorageInteractor

    private var filterEnabled: Boolean = true

    @AfterViews
    fun init() {
        profilePaymentsMenuLayoutView.setCurrentMenuItem(item = AppMenuView.Item.MY_PROFILE)

        initHeader()

        profilePaymentsSecondaryHeaderView.bind(
                ProfileHeaderView.Item.PAYMENTS,
                hasLessonsAlert = alertsProfileService.haveLessonsAlerts(),
                hasDebtsAlert = alertsProfileService.haveDebtsAlerts(),
                hasPaymentsAlert = alertsProfileService.havePaymentsAlerts()
        )

        profilePaymentsEmptyWithFilterView.bind {
            toggleFilter()
        }

        initList()
    }

    private fun initHeader() {
        profilePaymentsHeaderView
                .setLeftButtonAction { profilePaymentsMenuLayoutView.openMenu() }

        profilePaymentsHeaderView.getFirstButtonHandler()
                .setAction(action = { toggleFilter() })
                .setToggled(toggled = filterEnabled)
    }

    private fun toggleFilter() {
        filterEnabled = !filterEnabled

        profilePaymentsHeaderView.getFirstButtonHandler().setToggled(toggled = filterEnabled)

        initList()
    }

    private fun initList() {
        val teacherLogin = profileService.getMe()?.login

        val allPayments = teacherLogin?.let {
            studentsPaymentsProviderService.getForTeacher(
                    teacherLogin = teacherLogin,
                    onlyUnprocessed = false
            )
        } ?: emptyList()

        val filteredPayments = allPayments.filter { payment -> !filterEnabled || !payment.processed }

        profilePaymentsEmptyView.visibility = visibleElseGone(visible = allPayments.isEmpty())
        profilePaymentsEmptyWithFilterView.visibility = visibleElseGone(visible = (allPayments.isNotEmpty() && filteredPayments.isEmpty()))

        profilePaymentsTeacherPaymentsView.bind(
                data = PaymentsViewData(
                        paymentItemsData = filteredPayments
                                .sortedByDescending { payment ->
                                    payment.info.time
                                }.map { studentPayment ->
                                    PaymentsItemViewData(
                                            studentPayment = studentPayment,
                                            studentName = studentsStorageInteractor.getByLogin(
                                                    studentPayment.info.studentLogin
                                            )?.person?.name ?: "?",
                                            staffMemberName = staffMembersStorageService.getStaffMember(
                                                    studentPayment.info.staffMemberLogin
                                            )?.person?.name ?: "?",
                                            singleTeacher = false,
                                            clickAction = null
                                    )
                                }
                )
        )
    }

    override fun getAppLayoutView(): AppLayoutView? {
        return null
    }
}
