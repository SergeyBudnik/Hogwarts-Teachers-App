package com.bdev.hengschoolteacher.ui.activities.profile

import android.os.Bundle
import android.os.PersistableBundle
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.data.school.staff.StaffMember
import com.bdev.hengschoolteacher.interactors.alerts.profile.AlertsProfileInteractor
import com.bdev.hengschoolteacher.interactors.profile.ProfileInteractor
import com.bdev.hengschoolteacher.interactors.students.StudentsStorageInteractor
import com.bdev.hengschoolteacher.interactors.students_debts.StudentsDebtsInteractor
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import com.bdev.hengschoolteacher.ui.utils.RedirectBuilder
import com.bdev.hengschoolteacher.ui.views.app.AppLayoutView
import com.bdev.hengschoolteacher.ui.views.app.AppMenuView
import com.bdev.hengschoolteacher.ui.views.app.profile.ProfileHeaderView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_profile_depts.*
import javax.inject.Inject

@AndroidEntryPoint
class ProfileDebtsActivity : BaseActivity() {
    companion object {
        fun redirectToSibling(from: BaseActivity) {
            RedirectBuilder
                    .redirect(from)
                    .to(ProfileDebtsActivity::class.java)
                    .goAndCloseCurrent()
        }
    }

    @Inject lateinit var profileInteractor: ProfileInteractor
    @Inject lateinit var studentsStorageInteractor: StudentsStorageInteractor
    @Inject lateinit var studentsDebtsInteractor: StudentsDebtsInteractor
    @Inject lateinit var alertsProfileService: AlertsProfileInteractor

    private var me: StaffMember? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_profile_depts)

        me = profileInteractor.getMe()

        profileDebtsLayoutView.setCurrentMenuItem(AppMenuView.Item.MY_PROFILE)

        profileDebtsHeaderView.setLeftButtonAction { profileDebtsLayoutView.openMenu() }
        profileDebtsSecondaryHeaderView.bind(
                ProfileHeaderView.Item.DEBTS,
                hasLessonsAlert = alertsProfileService.haveLessonsAlerts(),
                hasDebtsAlert = alertsProfileService.haveDebtsAlerts(),
                hasPaymentsAlert = alertsProfileService.havePaymentsAlerts()
        )

        profileDebtsListView.bind(
                studentsToExpectedDebt = studentsStorageInteractor.getAll()
                        .filter { it.managerLogin == me?.login }
                        .map {
                            Pair(it, studentsDebtsInteractor.getExpectedDebt(studentLogin = it.login))
                        },
                searchQuery = "",
                withDebtsOnly = true
        )
    }

    override fun getAppLayoutView(): AppLayoutView = profileDebtsLayoutView
}