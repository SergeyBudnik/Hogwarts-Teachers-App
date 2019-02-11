package com.bdev.hengschoolteacher.ui.activities.monitoring

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.RelativeLayout
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.data.school.student.Student
import com.bdev.hengschoolteacher.service.StudentsAttendancesService
import com.bdev.hengschoolteacher.service.StudentsService
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import com.bdev.hengschoolteacher.ui.utils.RedirectUtils.Companion.redirect
import com.bdev.hengschoolteacher.ui.views.app.AppMenuView
import kotlinx.android.synthetic.main.activity_monitoring_payments.*
import kotlinx.android.synthetic.main.view_monitoring_payments_item.view.*
import org.androidannotations.annotations.*

@EViewGroup(R.layout.view_monitoring_payments_item)
open class MonitoringPaymentsItemView : RelativeLayout {
    lateinit var studentsAttendancesService: StudentsAttendancesService

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    fun bind(student: Student) {
        monitoringPaymentsItemNameView.text = student.name
    }
}

@EBean
open class MonitoringPaymentsListAdapter : BaseAdapter() {
    @RootContext
    lateinit var context: Context

    private var students: List<Student> = emptyList()

    fun setItems(students: List<Student>) {
        this.students = students.sortedBy { it.name }
    }

    override fun getView(position: Int, convertView: View?, parentView: ViewGroup): View {
        val v = if (convertView == null) {
            MonitoringPaymentsItemView_.build(context)
        } else {
            convertView as MonitoringPaymentsItemView
        }

        v.bind(getItem(position))

        return v
    }

    override fun getItem(position: Int): Student {
        return students[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return students.size
    }
}

@SuppressLint("Registered")
@EActivity(R.layout.activity_monitoring_payments)
open class MonitoringPaymentsActivity : BaseActivity() {
    @Bean
    lateinit var studentsService: StudentsService

    @Bean
    lateinit var monitoringPaymentsListAdapter: MonitoringPaymentsListAdapter

    @AfterViews
    fun init() {
        monitoringPaymentsHeaderView.setLeftButtonAction { monitoringPaymentsMenuLayoutView.openMenu() }

        monitoringPaymentsMenuLayoutView.setCurrentMenuItem(AppMenuView.Item.MONITORING)

        monitoringPaymentsListAdapter.setItems(studentsService.getAllStudents())

        monitoringPaymentsListView.adapter = monitoringPaymentsListAdapter
        monitoringPaymentsListView.setOnItemClickListener { _, _, position, _ ->
            val student = monitoringPaymentsListAdapter.getItem(position)

            redirect(this)
                    .to(MonitoringStudentPaymentActivity_::class.java)
                    .withAnim(R.anim.slide_open_enter, R.anim.slide_open_exit)
                    .withExtra(MonitoringStudentPaymentActivity.EXTRA_STUDENT_ID, student.id)
                    .go()
        }
    }
}
