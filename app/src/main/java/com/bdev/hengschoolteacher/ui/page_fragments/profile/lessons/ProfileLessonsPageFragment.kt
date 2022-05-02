package com.bdev.hengschoolteacher.ui.page_fragments.profile.lessons

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.ui.fragments.app_menu.AppMenuFragment
import com.bdev.hengschoolteacher.ui.fragments.app_menu.data.AppMenuItem
import com.bdev.hengschoolteacher.ui.fragments.profile.header.ProfileHeaderFragment
import com.bdev.hengschoolteacher.ui.fragments.profile.header.data.ProfileHeaderFragmentItem
import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragment
import com.bdev.hengschoolteacher.ui.utils.ViewVisibilityUtils.visibleElseGone
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_profile_lessons.*

@AndroidEntryPoint
class ProfileLessonsPageFragment : BasePageFragment<ProfileLessonsPageFragmentViewModel>() {
    override fun provideViewModel(): ProfileLessonsPageFragmentViewModel =
        ViewModelProvider(this).get(ProfileLessonsPageFragmentViewModelImpl::class.java)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.activity_profile_lessons, container, false)

    override fun doOnViewCreated() {
        super.doOnViewCreated()

        initSecondaryHeader()
        initMenu()

        fragmentViewModel.getDataLiveData().observe(this) { data ->
            bindHeader(data = data)
            bindLessonsList(data = data)
            bindWeekSelectionBar(data = data)
        }

        profileLessonsListView.bind()

        profileLessonsRootView.setCurrentMenuItem(AppMenuItem.MY_PROFILE)

        profileLessonsWeekSelectionBarView.init { weekIndex ->
            fragmentViewModel.setWeekIndex(weekIndex = weekIndex)
        }

        profileLessonsNoLessonsView.bind { fragmentViewModel.toggleFilter() }
    }

    private fun initSecondaryHeader() {
        getSecondaryHeaderFragment().setCurrentItem(
            item = ProfileHeaderFragmentItem.LESSONS
        )
    }

    private fun initMenu() {
        getMenu().setCurrentItem(item = AppMenuItem.MY_PROFILE)
    }

    private fun bindHeader(data: ProfileLessonsPageFragmentData) {
        profileLessonsHeaderView
                .setLeftButtonAction { profileLessonsRootView.openMenu() }

        profileLessonsHeaderView.getFirstButtonHandler()
                .setAction(action = { fragmentViewModel.toggleFilter() })
                .setToggled(toggled = data.filterEnabled)

        profileLessonsHeaderView.getSecondButtonHandler()
                .setAction(action = { fragmentViewModel.toggleCalendar() })
                .setToggled(toggled = data.calendarEnabled)
    }

    private fun bindLessonsList(data: ProfileLessonsPageFragmentData) {
        profileLessonsListView.fill(data = data.lessons)

        profileLessonsNoLessonsView.visibility = visibleElseGone(visible = data.noLessons)
    }

    private fun bindWeekSelectionBar(data: ProfileLessonsPageFragmentData) {
        profileLessonsWeekSelectionBarView.visibility = visibleElseGone(visible = data.calendarEnabled)
    }

    private fun getSecondaryHeaderFragment(): ProfileHeaderFragment =
        childFragmentManager.findFragmentById(R.id.profileLessonsSecondaryHeaderFragment) as ProfileHeaderFragment

    private fun getMenu(): AppMenuFragment =
        childFragmentManager.findFragmentById(R.id.appMenuFragment) as AppMenuFragment
}
