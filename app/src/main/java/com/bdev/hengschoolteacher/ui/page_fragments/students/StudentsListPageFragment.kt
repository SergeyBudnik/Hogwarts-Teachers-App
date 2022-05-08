package com.bdev.hengschoolteacher.ui.page_fragments.students

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.data.school.student.Student
import com.bdev.hengschoolteacher.interactors.students.StudentsStorageInteractor
import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragment
import com.bdev.hengschoolteacher.ui.page_fragments.students.adapters.StudentsListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_students_list.*
import javax.inject.Inject

@AndroidEntryPoint
class StudentsListPageFragment : BasePageFragment<StudentsListPageFragmentViewModel>() {
    @Inject lateinit var studentsStorageInteractor: StudentsStorageInteractor

    override fun provideViewModel(): StudentsListPageFragmentViewModel =
        ViewModelProvider(this).get(StudentsListPageFragmentViewModelImpl::class.java)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.activity_students_list, container, false)

    override fun doOnViewCreated() {
        super.doOnViewCreated()

        val studentsListAdapter = StudentsListAdapter(requireContext())

        initHeader()

        studentsListAdapter.setItems(studentsStorageInteractor.getAll().sortedBy { it.person.name })
        studentsListView.adapter = studentsListAdapter
        studentsListView.setOnItemClickListener { adapterView, _, position, _ ->
            (adapterView.getItemAtPosition(position) as Student).let { student ->
                fragmentViewModel.openStudentInformationPage(
                    login = student.login
                )
            }
        }

        studentsListHeaderSearchView.addOnTextChangeListener { filter ->
            studentsListAdapter.setFilter { student ->
                val nameMatches = student.person.name.toLowerCase().contains(filter.toLowerCase())
                val phoneMatches = student.person.contacts.phones.filter { it.value.contains(filter) }.any()

                return@setFilter nameMatches || phoneMatches
            }
            studentsListAdapter.notifyDataSetChanged()
        }
    }

    private fun initHeader() {
        studentsHeaderView
                .setLeftButtonAction { studentsMenuLayoutView.openMenu() }

        studentsHeaderView.getFirstButtonHandler()
                .setAction(action = { studentsListHeaderSearchView.show() })
    }
}
