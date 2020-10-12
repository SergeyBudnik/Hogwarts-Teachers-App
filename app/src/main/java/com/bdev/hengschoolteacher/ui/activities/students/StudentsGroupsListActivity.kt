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
import com.bdev.hengschoolteacher.service.GroupsService
import com.bdev.hengschoolteacher.service.StudentsService
import com.bdev.hengschoolteacher.service.profile.ProfileService
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
import org.androidannotations.annotations.EViewGroup
import java.util.*

@EViewGroup(R.layout.view_list_item_students_groups)
open class StudentsGroupsListItemView : LinearLayout {
    @Bean
    lateinit var studentsService: StudentsService
    @Bean
    lateinit var profileService: ProfileService

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    fun bind(group: Group): StudentsGroupsListItemView {
        setTitle(group)
        setBackground(group)

        return this
    }

    private fun setTitle(group: Group) {
        val groupStudents = studentsService.getGroupStudents(group.id)

        studentsGroupsItemNameView.text = groupStudents.foldRight("") {
            student, sum -> "${student.person.name.split(" ")[0]}; $sum"
        }
    }

    private fun setBackground(group: Group) {
        val me = profileService.getMe()

        if (me != null) {
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
}

class StudentsGroupsListAdapter(context: Context): BaseItemsListAdapter<Group>(context) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return if (convertView == null) {
            StudentsGroupsListItemView_.build(context)
        } else {
            (convertView as StudentsGroupsListItemView)
        }.bind(getItem(position))
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

    @Bean
    lateinit var groupsService: GroupsService
    @Bean
    lateinit var studentsService: StudentsService
    @Bean
    lateinit var profileService: ProfileService

    @AfterViews
    fun init() {
        studentsGroupsHeaderView
                .setLeftButtonAction { studentsGroupsMenuLayoutView.openMenu() }
                .setFirstRightButtonAction { studentsGroupsHeaderSearchView.show() }

        studentsGroupsMenuLayoutView.setCurrentMenuItem(AppMenuView.Item.STUDENTS)

        val me = profileService.getMe()

        if (me != null) {
            val adapter = StudentsGroupsListAdapter(this)

            adapter.setItems(groupsService
                    .getGroups()
                    .sortedByDescending { group -> group.lessons.filter { it.teacherLogin == me.login }.any() }
            )

            studentsGroupsListView.adapter = adapter

            studentsGroupsHeaderSearchView.addOnTextChangeListener { filter ->
                adapter.setFilter { group ->
                    val groupStudents = studentsService.getGroupStudents(group.id)

                    return@setFilter groupStudents.filter {
                        it.person.name.toLowerCase(Locale.getDefault()).contains(filter.toLowerCase(Locale.getDefault()))
                    }.any()
                }

                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun getAppLayoutView(): AppLayoutView? {
        return null
    }
}
