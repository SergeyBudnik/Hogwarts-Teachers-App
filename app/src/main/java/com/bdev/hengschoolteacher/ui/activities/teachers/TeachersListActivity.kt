package com.bdev.hengschoolteacher.ui.activities.teachers

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.RelativeLayout
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.data.school.teacher.Teacher
import com.bdev.hengschoolteacher.service.teacher.TeacherStorageService
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import com.bdev.hengschoolteacher.ui.activities.teacher.TeacherActivity
import com.bdev.hengschoolteacher.ui.views.app.AppMenuView
import kotlinx.android.synthetic.main.activity_teachers_list.*
import kotlinx.android.synthetic.main.view_teachers_list_row_item.view.*
import org.androidannotations.annotations.*

@EViewGroup(R.layout.view_teachers_list_row_item)
open class TeachersListRowItemView : RelativeLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    fun bind(teacher: Teacher): TeachersListRowItemView {
        teachersNameView.text = teacher.name

        return this
    }
}

@EBean
open class TeachersListAdapter : BaseAdapter() {
    @RootContext
    lateinit var context: Context

    private var teachers: List<Teacher> = ArrayList()

    fun setTeachers(students: List<Teacher>) {
        this.teachers = students.sortedBy { it.name }
    }

    override fun getView(position: Int, convertView: View?, parentView: ViewGroup): View {
        return if (convertView == null) {
            TeachersListRowItemView_.build(context)
        } else {
            convertView as TeachersListRowItemView
        }.bind(getItem(position))
    }

    override fun getItem(position: Int): Teacher {
        return teachers[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return teachers.size
    }
}

@SuppressLint("Registered")
@EActivity(R.layout.activity_teachers_list)
open class TeachersListActivity : BaseActivity() {
    @Bean
    lateinit var teacherStorageService: TeacherStorageService

    @Bean
    lateinit var teachersListAdapter: TeachersListAdapter

    @AfterViews
    fun init() {
        teachersHeaderView.setLeftButtonAction { teachersMenuLayoutView.openMenu() }
        teachersMenuLayoutView.setCurrentMenuItem(AppMenuView.Item.TEACHERS)

        teachersListAdapter.setTeachers(teacherStorageService.getAllTeachers())

        teachersListView.adapter = teachersListAdapter
        teachersListView.setOnItemClickListener { adapterView, _, position, _ ->
            val teacher = adapterView.getItemAtPosition(position) as Teacher

            TeacherActivity
                    .redirect(
                            current = this,
                            teacherId = teacher.id
                    )
                    .withAnim(R.anim.slide_open_enter, R.anim.slide_open_exit)
                    .go()
        }
    }
}
