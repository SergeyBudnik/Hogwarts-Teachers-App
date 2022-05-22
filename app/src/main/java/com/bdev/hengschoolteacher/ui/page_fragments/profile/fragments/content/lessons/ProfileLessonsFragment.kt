package com.bdev.hengschoolteacher.ui.page_fragments.profile.fragments.content.lessons

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.ui.fragments.lessons.LessonsListFragment
import com.bdev.hengschoolteacher.ui.page_fragments.profile.fragments.content.ProfileContentFragment
import com.bdev.hengschoolteacher.ui.utils.ViewVisibilityUtils.visibleElseGone
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_profile_lessons.*

@AndroidEntryPoint
class ProfileLessonsFragment : ProfileContentFragment<ProfileLessonsPageFragmentViewModel>() {
    override fun provideViewModel() =
        ViewModelProvider(this).get(ProfileLessonsPageFragmentViewModelImpl::class.java)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.fragment_profile_lessons, container, false)

    override fun getRootView(): View = profileLessonsRootView

    override fun doOnViewCreated() {
        super.doOnViewCreated()

        initLessonsList()

        fragmentViewModel.getDataLiveData().observe(this) { data ->
            bindLessonsList(data = data)
            bindWeekSelectionBar(data = data)
        }
        
        profileLessonsWeekSelectionBarView.init { weekIndex ->
            fragmentViewModel.setWeekIndex(weekIndex = weekIndex)
        }
    }

    private fun initLessonsList() {
        profileLessonsListView.getFragment<LessonsListFragment>().init(
            disableFilterAction = {
                fragmentViewModel.disableFilter()
            }
        )
    }

    private fun bindLessonsList(data: ProfileLessonsFragmentData) {
        profileLessonsListView.getFragment<LessonsListFragment>().update(
            lessons = data.lessons,
            weekIndex = data.weekIndex,
            filterEnabled = data.filterEnabled
        )
    }

    private fun bindWeekSelectionBar(data: ProfileLessonsFragmentData) {
        profileLessonsWeekSelectionBarView.visibility = visibleElseGone(visible = data.calendarEnabled)
    }
}