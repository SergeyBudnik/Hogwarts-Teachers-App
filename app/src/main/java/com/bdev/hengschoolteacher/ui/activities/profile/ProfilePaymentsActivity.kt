package com.bdev.hengschoolteacher.ui.activities.profile

import android.os.Bundle
import android.os.PersistableBundle
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.interactors.alerts.profile.AlertsProfileInteractor
import com.bdev.hengschoolteacher.interactors.profile.ProfileInteractor
import com.bdev.hengschoolteacher.interactors.staff_members.StaffMembersStorageInteractor
import com.bdev.hengschoolteacher.interactors.students.StudentsStorageInteractor
import com.bdev.hengschoolteacher.interactors.students_payments.StudentsPaymentsProviderInteractor
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import com.bdev.hengschoolteacher.ui.utils.RedirectBuilder
import com.bdev.hengschoolteacher.ui.utils.ViewVisibilityUtils.visibleElseGone
import com.bdev.hengschoolteacher.ui.views.app.AppLayoutView
import com.bdev.hengschoolteacher.ui.views.app.AppMenuView
import com.bdev.hengschoolteacher.ui.views.app.payments.PaymentsItemViewData
import com.bdev.hengschoolteacher.ui.views.app.payments.PaymentsViewData
import com.bdev.hengschoolteacher.ui.views.app.profile.ProfileHeaderView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_profile_payments.*
import javax.inject.Inject

@AndroidEntryPoint
class ProfilePaymentsActivity : BaseActivity() {
    companion object {
        fun redirectToSibling(current: BaseActivity) {
            RedirectBuilder
                    .redirect(current)
                    .to(ProfilePaymentsActivity::class.java)
                    .goAndCloseCurrent()
        }
    }

    @Inject lateinit var profileInteractor: ProfileInteractor
    @Inject lateinit var studentsPaymentsProviderInteractor: StudentsPaymentsProviderInteractor
    @Inject lateinit var alertsProfileService: AlertsProfileInteractor
    @Inject lateinit var staffMembersStorageInteractor: StaffMembersStorageInteractor
    @Inject lateinit var studentsStorageInteractor: StudentsStorageInteractor

    private var filterEnabled: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_profile_payments)

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
        val teacherLogin = profileInteractor.getMe()?.login

        val allPayments = teacherLogin?.let {
            studentsPaymentsProviderInteractor.getForTeacher(
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
                                            staffMemberName = staffMembersStorageInteractor.getStaffMember(
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
