package com.bdev.hengschoolteacher.ui.activities.students

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.data.school.student.Student
import com.bdev.hengschoolteacher.service.StudentsService
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import com.bdev.hengschoolteacher.ui.activities.student.StudentInformationActivity
import com.bdev.hengschoolteacher.ui.activities.student.StudentInformationActivity_
import com.bdev.hengschoolteacher.ui.adapters.BaseItemsAdapter
import com.bdev.hengschoolteacher.ui.utils.RedirectUtils.Companion.redirect
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
        studentNameView.text = student.name

        return this
    }
}

class StudentsListAdapter(context: Context) : BaseItemsAdapter<Student>(context) {
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
    @Bean
    lateinit var studentsService: StudentsService

    private var studentsListAdapter: StudentsListAdapter = StudentsListAdapter(this)

    @AfterViews
    fun init() {
        studentsHeaderView
                .setLeftButtonAction { studentsMenuLayoutView.openMenu() }
                .setFirstRightButtonAction { studentsListHeaderSearchView.show() }

        studentsMenuLayoutView.setCurrentMenuItem(AppMenuView.Item.STUDENTS)

        studentsListAdapter.setItems(studentsService.getAllStudents().sortedBy { it.name })
        studentsListView.adapter = studentsListAdapter
        studentsListView.setOnItemClickListener { adapterView, _, position, _ ->
            val student = adapterView.getItemAtPosition(position) as Student

            redirect(this)
                    .to(StudentInformationActivity_::class.java)
                    .withAnim(R.anim.slide_open_enter, R.anim.slide_open_exit)
                    .withExtra(StudentInformationActivity.EXTRA_STUDENT_ID, student.id)
                    .go()
        }

        studentsListHeaderSearchView.addOnTextChangeListener { filter ->
            studentsListAdapter.setFilter { student ->
                val nameMatches = student.name.toLowerCase().contains(filter.toLowerCase())
                val phoneMatches = student.phones.filter { it.contains(filter) }.any()

                return@setFilter nameMatches || phoneMatches
            }
            studentsListAdapter.notifyDataSetChanged()
        }
    }
}
