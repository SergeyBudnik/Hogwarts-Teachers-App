package com.bdev.hengschoolteacher.ui.page_fragments.teachers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.data.school.staff.StaffMember
import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragment
import com.bdev.hengschoolteacher.ui.page_fragments.teachers.adapters.TeachersListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.page_fragment_teachers_list.*

@AndroidEntryPoint
class TeachersListPageFragment : BasePageFragment<TeachersListPageFragmentViewModel>() {
    override fun provideViewModel(): TeachersListPageFragmentViewModel =
        ViewModelProvider(this).get(TeachersListPageFragmentViewModelImpl::class.java)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.page_fragment_teachers_list, container, false)

    override fun doOnViewCreated() {
        super.doOnViewCreated()

        initHeader()

        val teachersListAdapter = initList()

        fragmentViewModel.getDataLiveData().observe(this) { data ->
            updateList(
                adapter = teachersListAdapter,
                data = data
            )
        }
    }

    private fun initHeader() {
        teachersHeaderView.setLeftButtonAction { teachersMenuLayoutView.openMenu() }
    }

    private fun initList(): TeachersListAdapter {
        val teachersListAdapter = TeachersListAdapter(context = requireContext())

        teachersListView.adapter = teachersListAdapter
        teachersListView.setOnItemClickListener { adapterView, _, position, _ ->
            fragmentViewModel.openTeacherPage(
                teacher = adapterView.getItemAtPosition(position) as StaffMember
            )
        }

        return teachersListAdapter
    }

    private fun updateList(adapter: TeachersListAdapter, data: TeachersListPageFragmentData) {
        adapter.setTeachers(teachers = data.teachers)
    }
}
