package com.bdev.hengschoolteacher.ui.activities.profile

import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.data.school.staff.StaffMember
import com.bdev.hengschoolteacher.service.StudentsService
import com.bdev.hengschoolteacher.service.profile.ProfileService
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
    @Bean
    lateinit var studentsService: StudentsService

    private var me: StaffMember? = null

    @AfterViews
    fun afterViews() {
        me = profileService.getMe()

        profileDebtsLayoutView.setCurrentMenuItem(AppMenuView.Item.MY_PROFILE)

        profileDebtsHeaderView.setLeftButtonAction { profileDebtsLayoutView.openMenu() }
        profileDebtsSecondaryHeaderView.bind(ProfileHeaderView.Item.DEBTS)

        profileDebtsListView.bind(
                students = studentsService.getAllStudents().filter { it.managerLogin == me?.login },
                searchQuery = "",
                withDebtsOnly = true
        )
    }

    override fun getAppLayoutView(): AppLayoutView = profileDebtsLayoutView
}