package com.bdev.hengschoolteacher.ui.views.app.students

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.data.school.student.Student
import com.bdev.hengschoolteacher.services.students_debts.StudentDebtsService
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import com.bdev.hengschoolteacher.ui.activities.student.StudentInformationActivity
import com.bdev.hengschoolteacher.ui.adapters.BaseItemsListAdapter
import com.bdev.hengschoolteacher.ui.utils.ViewVisibilityUtils.visibleElseGone
import kotlinx.android.synthetic.main.view_students_with_debts.view.*
import kotlinx.android.synthetic.main.view_students_with_debts_item.view.*
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EViewGroup
import java.util.*

@EViewGroup(R.layout.view_students_with_debts)
open class StudentsWithDebtsListView : RelativeLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    @Bean
    lateinit var studentDebtsService: StudentDebtsService

    fun bind(students: List<Student>, searchQuery: String, withDebtsOnly: Boolean) {
        val adapter = StudentsListAdapter(context)

        adapter.setItems(
                students
                        .map {
                            StudentInfo(
                                    student = it,
                                    dept = studentDebtsService.getExpectedDebt(studentLogin = it.login).toLong()
                            )
                        }
                        .filter { studentInfo ->
                            val formattedSearchQuery = searchQuery.toLowerCase(Locale.getDefault())

                            return@filter if (searchQuery.isNotEmpty()) {
                                val student = studentInfo.student

                                val nameMatches = student.person.name.toLowerCase(Locale.getDefault()).contains(formattedSearchQuery)
                                val phoneMatches = student.person.contacts.phones.filter { phone -> phone.value.contains(formattedSearchQuery) }.any()

                                nameMatches || phoneMatches
                            } else {
                                !withDebtsOnly || studentInfo.dept > 0
                            }
                        }
                        .sortedBy { it.student.person.name }
        )

        studentsWithDebtsListView.adapter = adapter

        studentsWithDebtsListView.setOnItemClickListener { _, _, position, _ ->
            StudentInformationActivity.redirectToChild(
                    current = context as BaseActivity,
                    studentLogin = adapter.getItem(position).student.login
            )
        }
    }
}

@EViewGroup(R.layout.view_students_with_debts_item)
open class StudentsWithDebtsItemView : RelativeLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    fun bind(studentInfo: StudentInfo): StudentsWithDebtsItemView {
        studentsWithDebtsItemNameView.text = studentInfo.student.person.name
        studentsWithDebtsItemIconView.visibility = visibleElseGone(visible = studentInfo.dept > 0)

        return this
    }
}

private class StudentsListAdapter(context: Context) : BaseItemsListAdapter<StudentInfo>(context) {
    override fun getView(position: Int, convertView: View?, parentView: ViewGroup): View {
        return if (convertView == null) {
            StudentsWithDebtsItemView_.build(context)
        } else {
            convertView as StudentsWithDebtsItemView
        }.bind(
                studentInfo = getItem(position = position)
        )
    }
}

data class StudentInfo(
        val student: Student,
        val dept: Long
)