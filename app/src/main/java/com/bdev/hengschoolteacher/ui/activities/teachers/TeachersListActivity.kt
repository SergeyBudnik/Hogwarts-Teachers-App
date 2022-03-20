package com.bdev.hengschoolteacher.ui.activities.teachers

import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.RelativeLayout
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.data.school.staff.StaffMember
import com.bdev.hengschoolteacher.interactors.staff_members.StaffMembersStorageInteractor
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import com.bdev.hengschoolteacher.ui.activities.teacher.TeacherActivity
import com.bdev.hengschoolteacher.ui.utils.RedirectBuilder
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
class TeachersListActivity : BaseActivity() {
    companion object {
        fun redirectToSibling(current: BaseActivity) {
            RedirectBuilder
                    .redirect(current)
                    .to(TeachersListActivity::class.java)
                    .goAndCloseCurrent()
        }
    }

    @Inject lateinit var staffMembersStorageInteractor: StaffMembersStorageInteractor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_teachers_list)

        teachersHeaderView.setLeftButtonAction { teachersMenuLayoutView.openMenu() }
        teachersMenuLayoutView.setCurrentMenuItem(AppMenuView.Item.TEACHERS)

        val teachersListAdapter = TeachersListAdapter(context = this)

        teachersListAdapter.setTeachers(staffMembersStorageInteractor.getAllStaffMembers())

        teachersListView.adapter = teachersListAdapter
        teachersListView.setOnItemClickListener { adapterView, _, position, _ ->
            val teacher = adapterView.getItemAtPosition(position) as StaffMember

            TeacherActivity.redirectToChild(
                    current = this,
                    teacherLogin = teacher.login
            )
        }
    }

    override fun getAppLayoutView(): AppLayoutView? {
        return null
    }
}
