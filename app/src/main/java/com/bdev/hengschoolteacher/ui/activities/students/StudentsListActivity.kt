package com.bdev.hengschoolteacher.ui.activities.students

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.data.school.student.Student
import com.bdev.hengschoolteacher.services.students.StudentsStorageService
import com.bdev.hengschoolteacher.services.students.StudentsStorageServiceImpl
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import com.bdev.hengschoolteacher.ui.activities.student.StudentInformationActivity
import com.bdev.hengschoolteacher.ui.adapters.BaseItemsListAdapter
import com.bdev.hengschoolteacher.ui.utils.RedirectBuilder
import com.bdev.hengschoolteacher.ui.views.app.AppLayoutView
import com.bdev.hengschoolteacher.ui.views.app.AppMenuView
import kotlinx.android.synthetic.main.activity_students_list.*
import kotlinx.android.synthetic.main.view_students_list_row_item.view.*
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EActivity
import org.androidannotations.annotations.EViewGroup

@EViewGroup(R.layout.view_students_list_row_item)
open class StudentsListRowItemView : RelativeLayout {
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
            StudentsListRowItemView_.build(context)
        } else {
            convertView as StudentsListRowItemView
        }.bind(getItem(position))
    }
}

@SuppressLint("Registered")
@EActivity(R.layout.activity_students_list)
open class StudentsListActivity : BaseActivity() {
    companion object {
        fun redirectToSibling(current: BaseActivity) {
            RedirectBuilder
                    .redirect(current)
                    .to(StudentsListActivity_::class.java)
                    .goAndCloseCurrent()
        }
    }

    @Bean(StudentsStorageServiceImpl::class)
    lateinit var studentsStorageService: StudentsStorageService

    private var studentsListAdapter: StudentsListAdapter = StudentsListAdapter(this)

    @AfterViews
    fun init() {
        studentsHeaderView
                .setLeftButtonAction { studentsMenuLayoutView.openMenu() }
                .setFirstRightButtonAction { studentsListHeaderSearchView.show() }

        studentsMenuLayoutView.setCurrentMenuItem(AppMenuView.Item.STUDENTS)

        studentsListAdapter.setItems(studentsStorageService.getAll().sortedBy { it.person.name })
        studentsListView.adapter = studentsListAdapter
        studentsListView.setOnItemClickListener { adapterView, _, position, _ ->
            val student = adapterView.getItemAtPosition(position) as Student

            StudentInformationActivity.redirectToChild(
                    current = this,
                    studentLogin = student.login
            )
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

    override fun getAppLayoutView(): AppLayoutView? {
        return null
    }
}
