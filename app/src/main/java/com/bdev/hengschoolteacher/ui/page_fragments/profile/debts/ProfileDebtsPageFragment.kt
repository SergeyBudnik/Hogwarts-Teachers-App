package com.bdev.hengschoolteacher.ui.page_fragments.profile.debts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.interactors.alerts.profile.AlertsProfileInteractor
import com.bdev.hengschoolteacher.interactors.profile.ProfileInteractor
import com.bdev.hengschoolteacher.interactors.students.StudentsStorageInteractor
import com.bdev.hengschoolteacher.interactors.students_debts.StudentsDebtsInteractor
import com.bdev.hengschoolteacher.ui.fragments.app_menu.AppMenuFragment
import com.bdev.hengschoolteacher.ui.fragments.app_menu.data.AppMenuItem
import com.bdev.hengschoolteacher.ui.fragments.profile.header.ProfileHeaderFragment
import com.bdev.hengschoolteacher.ui.fragments.profile.header.data.ProfileHeaderFragmentItem
import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_profile_depts.*
import javax.inject.Inject

@AndroidEntryPoint
class ProfileDebtsPageFragment : BasePageFragment<ProfileDebtsPageFragmentViewModel>() {
    @Inject lateinit var profileInteractor: ProfileInteractor
    @Inject lateinit var studentsStorageInteractor: StudentsStorageInteractor
    @Inject lateinit var studentsDebtsInteractor: StudentsDebtsInteractor
    @Inject lateinit var alertsProfileService: AlertsProfileInteractor

    override fun provideViewModel(): ProfileDebtsPageFragmentViewModel =
        ViewModelProvider(this).get(ProfileDebtsPageFragmentViewModelImpl::class.java)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.activity_profile_depts, container, false)

    override fun doOnViewCreated() {
        super.doOnViewCreated()

        getSecondaryHeaderFragment().setCurrentItem(item = ProfileHeaderFragmentItem.DEBTS)
        getMenu().setCurrentItem(item = AppMenuItem.MY_PROFILE)

        val me = profileInteractor.getMe()

        profileDebtsLayoutView.setCurrentMenuItem(AppMenuItem.MY_PROFILE)

        profileDebtsHeaderView.setLeftButtonAction { profileDebtsLayoutView.openMenu() }

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

    private fun getSecondaryHeaderFragment(): ProfileHeaderFragment =
        childFragmentManager.findFragmentById(R.id.profileDebtsSecondaryHeaderFragment) as ProfileHeaderFragment

    private fun getMenu(): AppMenuFragment =
        childFragmentManager.findFragmentById(R.id.appMenuFragment) as AppMenuFragment
}