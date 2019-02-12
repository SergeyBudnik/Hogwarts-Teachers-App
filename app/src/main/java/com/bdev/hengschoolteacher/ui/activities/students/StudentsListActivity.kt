package com.bdev.hengschoolteacher.ui.activities.students

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.RelativeLayout
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.data.school.student.Student
import com.bdev.hengschoolteacher.service.StudentsService
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import com.bdev.hengschoolteacher.ui.activities.student.StudentInformationActivity
import com.bdev.hengschoolteacher.ui.activities.student.StudentInformationActivity_
import com.bdev.hengschoolteacher.ui.utils.RedirectUtils.Companion.redirect
import com.bdev.hengschoolteacher.ui.views.app.AppMenuView
import kotlinx.android.synthetic.main.activity_students_list.*
import kotlinx.android.synthetic.main.view_students_list_row_item.view.*
import org.androidannotations.annotations.*

@EViewGroup(R.layout.view_students_list_row_item)
open class StudentsListRowItemView : RelativeLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    fun bind(student: Student): StudentsListRowItemView {
        studentNameView.text = student.name

        return this
    }
}

@EBean
open class StudentsListAdapter : BaseAdapter() {
    @RootContext
    lateinit var context: Context

    private var students: List<Student> = ArrayList()
    private var filter: String = ""

    private var filteredStudents: List<Student> = ArrayList()

    fun setStudents(students: List<Student>) {
        this.students = students.sortedBy { it.name }

        this.filteredStudents = getFilteredStudents()
    }

    fun setFilter(filter: String) {
        this.filter = filter

        this.filteredStudents = getFilteredStudents()
    }

    override fun getView(position: Int, convertView: View?, parentView: ViewGroup): View {
        return if (convertView == null) {
            StudentsListRowItemView_.build(context)
        } else {
            convertView as StudentsListRowItemView
        }.bind(getItem(position))
    }

    override fun getItem(position: Int): Student {
        return filteredStudents[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return filteredStudents.size
    }

    private fun getFilteredStudents(): List<Student> {
        return students.filter { it.name.toLowerCase().contains(filter) }
    }
}

@SuppressLint("Registered")
@EActivity(R.layout.activity_students_list)
open class StudentsListActivity : BaseActivity() {
    @Bean
    lateinit var studentsService: StudentsService

    @Bean
    lateinit var studentsListAdapter: StudentsListAdapter

    @AfterViews
    fun init() {
        studentsHeaderView
                .setLeftButtonAction { studentsMenuLayoutView.openMenu() }
                .setRightButtonAction { studentsListHeaderSearchView.show() }

        studentsMenuLayoutView.setCurrentMenuItem(AppMenuView.Item.STUDENTS)

        studentsListAdapter.setStudents(studentsService.getAllStudents())
        studentsListView.adapter = studentsListAdapter
        studentsListView.setOnItemClickListener { adapterView, _, position, _ ->
            val student = adapterView.getItemAtPosition(position) as Student

            redirect(this)
                    .to(StudentInformationActivity_::class.java)
                    .withAnim(R.anim.slide_open_enter, R.anim.slide_open_exit)
                    .withExtra(StudentInformationActivity.EXTRA_STUDENT_ID, student.id)
                    .go()
        }

        studentsListHeaderSearchView.addOnTextChangeListener {
            studentsListAdapter.setFilter(it)
            studentsListAdapter.notifyDataSetChanged()
        }
    }
}
