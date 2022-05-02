package com.bdev.hengschoolteacher.ui.page_fragments.students.groups

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.lifecycle.ViewModelProvider
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.data.school.group.Group
import com.bdev.hengschoolteacher.data.school.staff.StaffMember
import com.bdev.hengschoolteacher.data.school.student.Student
import com.bdev.hengschoolteacher.interactors.groups.GroupsStorageInteractor
import com.bdev.hengschoolteacher.interactors.groups.GroupsStudentsProviderInteractor
import com.bdev.hengschoolteacher.interactors.profile.ProfileInteractor
import com.bdev.hengschoolteacher.interactors.students.StudentsStorageInteractor
import com.bdev.hengschoolteacher.ui.adapters.BaseItemsListAdapter
import com.bdev.hengschoolteacher.ui.fragments.app_menu.data.AppMenuItem
import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragment
import com.bdev.hengschoolteacher.ui.resources.AppResources
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_students_groups.*
import kotlinx.android.synthetic.main.view_list_item_students_groups.view.*
import java.util.*
import javax.inject.Inject

data class StudentsGroupsListItemViewData(
        val me: StaffMember,
        val group: Group,
        val groupStudents: List<Student>
)

class StudentsGroupsListItemView : LinearLayout {
    init {
        View.inflate(context, R.layout.view_list_item_students_groups, this)
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    fun bind(data: StudentsGroupsListItemViewData): StudentsGroupsListItemView {
        setTitle(groupStudents = data.groupStudents)
        setBackground(me = data.me, group = data.group)

        return this
    }

    private fun setTitle(groupStudents: List<Student>) {
        studentsGroupsItemNameView.text = groupStudents.foldRight("") {
            student, sum -> "${student.person.name.split(" ")[0]}; $sum"
        }
    }

    private fun setBackground(me: StaffMember, group: Group) {
        val isMyGroup = group.lessons.filter { it.teacherLogin == me.login }.any()

        val colorId = if (isMyGroup) { R.color.status_info_subtle } else { R.color.alt_contrast_light }

        studentsGroupsItemView.backgroundTintList = ColorStateList.valueOf(
                AppResources.getColor(
                        context = context,
                        colorId = colorId
                )
        )
    }
}

class StudentsGroupsListAdapter(context: Context): BaseItemsListAdapter<StudentsGroupsListItemViewData>(context) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return if (convertView == null) {
            StudentsGroupsListItemView(context)
        } else {
            (convertView as StudentsGroupsListItemView)
        }.bind(data = getItem(position))
    }
}

@AndroidEntryPoint
class StudentsGroupsListPageFragment : BasePageFragment<StudentsGroupsListPageFragmentViewModel>() {
    @Inject lateinit var groupsStorageInteractor: GroupsStorageInteractor
    @Inject lateinit var groupsStudentsProviderInteractor: GroupsStudentsProviderInteractor
    @Inject lateinit var studentsStorageInteractor: StudentsStorageInteractor
    @Inject lateinit var profileInteractor: ProfileInteractor

    override fun provideViewModel(): StudentsGroupsListPageFragmentViewModel =
        ViewModelProvider(this).get(StudentsGroupsListPageFragmentViewModelImpl::class.java)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.activity_students_groups, container, false)

    override fun doOnViewCreated() {
        super.doOnViewCreated()

        val currentTime = Date().time

        initHeader()

        val me = profileInteractor.getMe()

        if (me != null) {
            val adapter = StudentsGroupsListAdapter(context = requireContext())

            adapter.setItems(groupsStorageInteractor
                    .getAll()
                    .sortedByDescending { group -> group.lessons.filter { it.teacherLogin == me.login }.any() }
                    .map { group ->
                        StudentsGroupsListItemViewData(
                                me = me,
                                group = group,
                                groupStudents = groupsStudentsProviderInteractor.getAll(
                                        groupId = group.id,
                                        time = currentTime
                                )

                        )
                    }
            )

            studentsGroupsListView.adapter = adapter

            studentsGroupsHeaderSearchView.addOnTextChangeListener { filter ->
                adapter.setFilter { data ->
                    val groupStudents = groupsStudentsProviderInteractor.getAll(
                            groupId = data.group.id,
                            time = currentTime
                    )

                    return@setFilter groupStudents.filter {
                        it.person.name.toLowerCase(Locale.getDefault()).contains(filter.toLowerCase(Locale.getDefault()))
                    }.any()
                }

                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun initHeader() {
        studentsGroupsHeaderView
                .setLeftButtonAction { studentsGroupsMenuLayoutView.openMenu() }

        studentsGroupsHeaderView.getFirstButtonHandler()
                .setAction(action = { studentsGroupsHeaderSearchView.show() })
    }
}
