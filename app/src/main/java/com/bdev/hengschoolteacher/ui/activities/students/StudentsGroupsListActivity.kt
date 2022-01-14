package com.bdev.hengschoolteacher.ui.activities.students

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.data.school.group.Group
import com.bdev.hengschoolteacher.data.school.staff.StaffMember
import com.bdev.hengschoolteacher.data.school.student.Student
import com.bdev.hengschoolteacher.services.groups.GroupStudentsProviderService
import com.bdev.hengschoolteacher.services.groups.GroupStudentsProviderServiceImpl
import com.bdev.hengschoolteacher.services.groups.GroupsStorageService
import com.bdev.hengschoolteacher.services.groups.GroupsStorageServiceImpl
import com.bdev.hengschoolteacher.services.profile.ProfileService
import com.bdev.hengschoolteacher.services.students.StudentsStorageService
import com.bdev.hengschoolteacher.services.students.StudentsStorageServiceImpl
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import com.bdev.hengschoolteacher.ui.adapters.BaseItemsListAdapter
import com.bdev.hengschoolteacher.ui.resources.AppResources
import com.bdev.hengschoolteacher.ui.utils.RedirectBuilder
import com.bdev.hengschoolteacher.ui.views.app.AppLayoutView
import com.bdev.hengschoolteacher.ui.views.app.AppMenuView
import kotlinx.android.synthetic.main.activity_students_groups.*
import kotlinx.android.synthetic.main.view_list_item_students_groups.view.*
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EActivity
import java.util.*

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

@SuppressLint("Registered")
@EActivity(R.layout.activity_students_groups)
open class StudentsGroupsListActivity : BaseActivity() {
    companion object {
        fun redirectToSibling(current: BaseActivity) {
            RedirectBuilder
                    .redirect(current)
                    .to(StudentsGroupsListActivity_::class.java)
                    .goAndCloseCurrent()
        }
    }

    @Bean(GroupsStorageServiceImpl::class)
    lateinit var groupsStorageService: GroupsStorageService
    @Bean(GroupStudentsProviderServiceImpl::class)
    lateinit var groupsStudentsProviderService: GroupStudentsProviderService
    @Bean(StudentsStorageServiceImpl::class)
    lateinit var studentsStorageService: StudentsStorageService
    @Bean
    lateinit var profileService: ProfileService

    @AfterViews
    fun init() {
        val currentTime = Date().time

        initHeader()

        studentsGroupsMenuLayoutView.setCurrentMenuItem(AppMenuView.Item.STUDENTS)

        val me = profileService.getMe()

        if (me != null) {
            val adapter = StudentsGroupsListAdapter(context = this)

            adapter.setItems(groupsStorageService
                    .getAll()
                    .sortedByDescending { group -> group.lessons.filter { it.teacherLogin == me.login }.any() }
                    .map { group ->
                        StudentsGroupsListItemViewData(
                                me = me,
                                group = group,
                                groupStudents = groupsStudentsProviderService.getAll(
                                        groupId = group.id,
                                        time = currentTime
                                )

                        )
                    }
            )

            studentsGroupsListView.adapter = adapter

            studentsGroupsHeaderSearchView.addOnTextChangeListener { filter ->
                adapter.setFilter { data ->
                    val groupStudents = groupsStudentsProviderService.getAll(
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

    override fun getAppLayoutView(): AppLayoutView? {
        return null
    }
}
