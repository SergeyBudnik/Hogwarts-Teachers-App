package com.bdev.hengschoolteacher.ui.page_fragments.students.list

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.lifecycle.ViewModelProvider
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.data.school.student.Student
import com.bdev.hengschoolteacher.interactors.students.StudentsStorageInteractor
import com.bdev.hengschoolteacher.ui.adapters.BaseItemsListAdapter
import com.bdev.hengschoolteacher.ui.fragments.app_menu.data.AppMenuItem
import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_students_list.*
import kotlinx.android.synthetic.main.view_students_list_row_item.view.*
import javax.inject.Inject

class StudentsListRowItemView : RelativeLayout {
    init {
        View.inflate(context, R.layout.view_students_list_row_item, this)
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    fun bind(student: Student): StudentsListRowItemView {
        studentNameView.text = student.person.name

        return this
    }
}

class StudentsListAdapter(context: Context) : BaseItemsListAdapter<Student>(context) {
    override fun getView(position: Int, convertView: View?, parentView: ViewGroup): View {
        return if (convertView == null) {
            StudentsListRowItemView(context)
        } else {
            convertView as StudentsListRowItemView
        }.bind(getItem(position))
    }
}

@AndroidEntryPoint
class StudentsListPageFragment : BasePageFragment<StudentsListPageFragmentViewModel>() {
    @Inject lateinit var studentsStorageInteractor: StudentsStorageInteractor

    private var studentsListAdapter: StudentsListAdapter = StudentsListAdapter(requireContext())

    override fun provideViewModel(): StudentsListPageFragmentViewModel =
        ViewModelProvider(this).get(StudentsListPageFragmentViewModelImpl::class.java)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.activity_students_list, container, false)

    override fun doOnViewCreated() {
        super.doOnViewCreated()

        initHeader()

        studentsListAdapter.setItems(studentsStorageInteractor.getAll().sortedBy { it.person.name })
        studentsListView.adapter = studentsListAdapter
        studentsListView.setOnItemClickListener { adapterView, _, position, _ ->
            val student = adapterView.getItemAtPosition(position) as Student

//            StudentInformationPageFragment.redirectToChild(
//                    current = this,
//                    studentLogin = student.login
//            )
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
