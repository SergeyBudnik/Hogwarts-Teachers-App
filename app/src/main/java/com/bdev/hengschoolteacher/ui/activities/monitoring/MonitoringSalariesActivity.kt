package com.bdev.hengschoolteacher.ui.activities.monitoring

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.RelativeLayout
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.data.school.teacher.Teacher
import com.bdev.hengschoolteacher.service.TeachersPaymentService
import com.bdev.hengschoolteacher.service.TeachersService
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import com.bdev.hengschoolteacher.ui.views.app.AppMenuView
import kotlinx.android.synthetic.main.activity_monitoring_salaries.*
import kotlinx.android.synthetic.main.view_monitoring_salaries_item.view.*
import org.androidannotations.annotations.*

@EViewGroup(R.layout.view_monitoring_salaries_item)
open class MonitoringSalariesItemView : RelativeLayout {
    @Bean
    lateinit var teachersPaymentService: TeachersPaymentService

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    fun bind(teacher: Teacher, weekIndex: Int) {
        val teacherPayments = teachersPaymentService.getTeacherPayments(teacher.id, weekIndex)

        monitoringSalariesItemNameView.text = teacher.name
        monitoringSalariesItemSalaryView.text = "${teacherPayments.fold(0) {v, tp -> v + tp.amount}} Р"
    }
}

@EBean
open class MonitoringSalariesListAdapter : BaseAdapter() {
    @RootContext
    lateinit var context: Context

    private var teachers: List<Teacher> = emptyList()

    fun setItems(teachers: List<Teacher>) {
        this.teachers = teachers
    }

    override fun getView(position: Int, convertView: View?, parentView: ViewGroup): View {
        val v = if (convertView == null) {
            MonitoringSalariesItemView_.build(context)
        } else {
            convertView as MonitoringSalariesItemView
        }

        v.bind(getItem(position), 0) // WeekIndex

        return v
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
@EActivity(R.layout.activity_monitoring_salaries)
open class MonitoringSalariesActivity : BaseActivity() {
    @Bean
    lateinit var teachersService: TeachersService

    @Bean
    lateinit var monitoringSalariesListAdapter: MonitoringSalariesListAdapter

    @AfterViews
    fun init() {
        monitoringSalariesHeaderView.setLeftButtonAction { monitoringSalariesMenuLayoutView.openMenu() }

        monitoringSalariesMenuLayoutView.setCurrentMenuItem(AppMenuView.Item.MONITORING)

        monitoringSalariesListAdapter.setItems(teachersService.getAllTeachers())
        monitoringSalariesListView.adapter = monitoringSalariesListAdapter
    }
}
