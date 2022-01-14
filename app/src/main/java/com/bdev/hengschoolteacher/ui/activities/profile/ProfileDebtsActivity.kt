package com.bdev.hengschoolteacher.ui.activities.profile

import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.data.school.staff.StaffMember
import com.bdev.hengschoolteacher.services.alerts.profile.AlertsProfileService
import com.bdev.hengschoolteacher.services.students.StudentsStorageService
import com.bdev.hengschoolteacher.services.profile.ProfileService
import com.bdev.hengschoolteacher.services.students.StudentsStorageServiceImpl
import com.bdev.hengschoolteacher.services.students_debts.StudentDebtsService
import com.bdev.hengschoolteacher.services.students_debts.StudentDebtsServiceImpl
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import com.bdev.hengschoolteacher.ui.utils.RedirectBuilder
import com.bdev.hengschoolteacher.ui.views.app.AppLayoutView
import com.bdev.hengschoolteacher.ui.views.app.AppMenuView
import com.bdev.hengschoolteacher.ui.views.app.profile.ProfileHeaderView
import kotlinx.android.synthetic.main.activity_profile_depts.*
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_profile_depts)
open class ProfileDebtsActivity : BaseActivity() {
    companion object {
        fun redirectToSibling(from: BaseActivity) {
            RedirectBuilder
                    .redirect(from)
                    .to(ProfileDebtsActivity_::class.java)
                    .goAndCloseCurrent()
        }
    }

    @Bean
    lateinit var profileService: ProfileService
    @Bean(StudentsStorageServiceImpl::class)
    lateinit var studentsStorageService: StudentsStorageService
    @Bean(StudentDebtsServiceImpl::class)
    lateinit var studentsDebtsService: StudentDebtsService
    @Bean
    lateinit var alertsProfileService: AlertsProfileService

    private var me: StaffMember? = null

    @AfterViews
    fun afterViews() {
        me = profileService.getMe()

        profileDebtsLayoutView.setCurrentMenuItem(AppMenuView.Item.MY_PROFILE)

        profileDebtsHeaderView.setLeftButtonAction { profileDebtsLayoutView.openMenu() }
        profileDebtsSecondaryHeaderView.bind(
                ProfileHeaderView.Item.DEBTS,
                hasLessonsAlert = alertsProfileService.haveLessonsAlerts(),
                hasDebtsAlert = alertsProfileService.haveDebtsAlerts(),
                hasPaymentsAlert = alertsProfileService.havePaymentsAlerts()
        )

        profileDebtsListView.bind(
                studentsToExpectedDebt = studentsStorageService.getAll()
                        .filter { it.managerLogin == me?.login }
                        .map {
                            Pair(it, studentsDebtsService.getExpectedDebt(studentLogin = it.login))
                        },
                searchQuery = "",
                withDebtsOnly = true
        )
    }

    override fun getAppLayoutView(): AppLayoutView = profileDebtsLayoutView
}