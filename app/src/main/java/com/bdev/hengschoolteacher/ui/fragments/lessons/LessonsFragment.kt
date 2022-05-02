package com.bdev.hengschoolteacher.ui.fragments.lessons

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.data.school.group.GroupAndLesson
import com.bdev.hengschoolteacher.ui.fragments.BaseFragment
import com.bdev.hengschoolteacher.ui.utils.ViewVisibilityUtils.visibleElseGone
import com.bdev.hengschoolteacher.ui.views.app.lessons.LessonsListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_lessons.*

@AndroidEntryPoint
class LessonsFragment : BaseFragment<LessonsFragmentViewModel>() {
    override fun provideViewModel() =
        ViewModelProvider(this).get(LessonsFragmentViewModelImpl::class.java)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.fragment_lessons, container, false)

    override fun doOnViewCreated() {
        super.doOnViewCreated()

        val adapter = LessonsListAdapter(context = requireContext())

        lessonsListView.adapter = adapter

        fragmentViewModel.getDataLiveData().observe(this) { data ->
            updateList(adapter = adapter, data = data)
        }
    }

    fun init(disableFilterAction: () -> Unit) {
        lessonsEmptyView.bind(disableFilterAction = disableFilterAction)
    }

    fun update(
        lessons: List<GroupAndLesson>,
        weekIndex: Int,
        filterEnabled: Boolean
    ) {
        fragmentViewModel.update(
            lessons = lessons,
            weekIndex = weekIndex,
            filterEnabled = filterEnabled
        )
    }

    private fun updateList(adapter: LessonsListAdapter, data: LessonsFragmentData) {
        adapter.setItems(data.lessons)
        adapter.setWeekIndex(data.weekIndex)
        adapter.notifyDataSetChanged()

        lessonsEmptyView.visibility = visibleElseGone(
            visible = (data.lessons.isEmpty() && data.filterEnabled)
        )
    }
}