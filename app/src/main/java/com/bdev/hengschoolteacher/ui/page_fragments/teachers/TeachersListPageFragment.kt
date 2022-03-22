package com.bdev.hengschoolteacher.ui.page_fragments.teachers

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.RelativeLayout
import androidx.lifecycle.ViewModelProvider
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.data.school.staff.StaffMember
import com.bdev.hengschoolteacher.interactors.staff_members.StaffMembersStorageInteractor
import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragment
import com.bdev.hengschoolteacher.ui.views.app.AppLayoutView
import com.bdev.hengschoolteacher.ui.views.app.AppMenuView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_teachers_list.*
import kotlinx.android.synthetic.main.view_teachers_list_row_item.view.*
import javax.inject.Inject

class TeachersListRowItemView : RelativeLayout {
    init {
        View.inflate(context, R.layout.view_teachers_list_row_item, this)
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    fun bind(staffMember: StaffMember): TeachersListRowItemView {
        teachersNameView.text = staffMember.person.name

        return this
    }
}

class TeachersListAdapter(
    private val context: Context
) : BaseAdapter() {
    private var teachers: List<StaffMember> = ArrayList()

    fun setTeachers(teachers: List<StaffMember>) {
        this.teachers = teachers.sortedBy { it.login }
    }

    override fun getView(position: Int, convertView: View?, parentView: ViewGroup): View {
        return if (convertView == null) {
            TeachersListRowItemView(context)
        } else {
            convertView as TeachersListRowItemView
        }.bind(getItem(position))
    }

    override fun getItem(position: Int): StaffMember {
        return teachers[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return teachers.size
    }
}

@AndroidEntryPoint
class TeachersListPageFragment : BasePageFragment<TeachersListPageFragmentViewModel>() {
    @Inject lateinit var staffMembersStorageInteractor: StaffMembersStorageInteractor

    override fun provideViewModel(): TeachersListPageFragmentViewModel =
        ViewModelProvider(this).get(TeachersListPageFragmentViewModelImpl::class.java)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.activity_teachers_list, container, false)

    override fun doOnViewCreated() {
        super.doOnViewCreated()

        teachersHeaderView.setLeftButtonAction { teachersMenuLayoutView.openMenu() }
        teachersMenuLayoutView.setCurrentMenuItem(AppMenuView.Item.TEACHERS)

        val teachersListAdapter = TeachersListAdapter(context = requireContext())

        teachersListAdapter.setTeachers(staffMembersStorageInteractor.getAllStaffMembers())

        teachersListView.adapter = teachersListAdapter
        teachersListView.setOnItemClickListener { adapterView, _, position, _ ->
            val teacher = adapterView.getItemAtPosition(position) as StaffMember

//            TeacherPageFragment.redirectToChild(
//                    current = this,
//                    teacherLogin = teacher.login
//            )
        }
    }

    override fun getAppLayoutView(): AppLayoutView? {
        return null
    }
}
