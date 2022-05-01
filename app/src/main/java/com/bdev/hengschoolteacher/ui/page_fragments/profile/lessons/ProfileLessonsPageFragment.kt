package com.bdev.hengschoolteacher.ui.page_fragments.profile.lessons

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragment
import com.bdev.hengschoolteacher.ui.utils.ViewVisibilityUtils.visibleElseGone
import com.bdev.hengschoolteacher.ui.views.app.root.HtPageRootView
import com.bdev.hengschoolteacher.ui.views.app.AppMenuView
import com.bdev.hengschoolteacher.ui.views.app.lessons.LessonItemView
import com.bdev.hengschoolteacher.ui.views.app.profile.ProfileHeaderView
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

        fragmentViewModel.getDataLiveData().observe(this) { data ->
            bindHeader(data = data)
            bindSecondaryHeader(data = data)
            bindLessonsList(data = data)
            bindWeekSelectionBar(data = data)
            bindMenu(data = data)
        }

        profileLessonsListView.bind()

        profileLessonsRootView.setCurrentMenuItem(AppMenuView.Item.MY_PROFILE)

        profileLessonsWeekSelectionBarView.init { weekIndex ->
            fragmentViewModel.setWeekIndex(weekIndex = weekIndex)
        }

        profileLessonsNoLessonsView.bind { fragmentViewModel.toggleFilter() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == LessonItemView.REQUEST_CODE_LESSON) {
            if (resultCode == Activity.RESULT_OK) {
                // bindLessonsList()
            }
        }
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

    private fun bindSecondaryHeader(data: ProfileLessonsPageFragmentData) {
        profileLessonsSecondaryHeaderView.bind(
            ProfileHeaderView.Item.LESSONS,
            hasLessonsAlert = data.hasLessonsAlert,
            hasDebtsAlert = data.hasDebtsAlert,
            hasPaymentsAlert = data.hasPaymentsAlert,
            navCommandHandler = { navCommand ->

            }
        )
    }

    private fun bindLessonsList(data: ProfileLessonsPageFragmentData) {
        profileLessonsListView.fill(data = data.lessons)

        profileLessonsNoLessonsView.visibility = visibleElseGone(visible = data.noLessons)
    }

    private fun bindWeekSelectionBar(data: ProfileLessonsPageFragmentData) {
        profileLessonsWeekSelectionBarView.visibility = visibleElseGone(visible = data.calendarEnabled)
    }

    private fun bindMenu(data: ProfileLessonsPageFragmentData) {
        profileLessonsRootView.getMenuView().bind(
            me = data.me,
            hasProfileAlerts = data.hasPaymentsAlert && data.hasLessonsAlert && data.hasDebtsAlert,
            hasMonitoringAlerts = false, // todo
            item = AppMenuView.Item.MY_PROFILE,
            navCommandHandler = { navCommand ->
                fragmentViewModel.navigate(navCommand = navCommand)
            }
        )
    }
}
