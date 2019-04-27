package com.bdev.hengschoolteacher.ui.activities.monitoring

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.data.school.student.StudentPayment
import com.bdev.hengschoolteacher.service.StudentsPaymentsService
import com.bdev.hengschoolteacher.service.StudentsService
import com.bdev.hengschoolteacher.service.TeachersService
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import com.bdev.hengschoolteacher.ui.adapters.BaseItemsListAdapter
import kotlinx.android.synthetic.main.activity_monitoring_teacher_payments.*
import kotlinx.android.synthetic.main.view_item_monitoring_teacher_payments.view.*
import org.androidannotations.annotations.*

@EViewGroup(R.layout.view_item_monitoring_teacher_payments)
open class MonitoringTeacherPaymentsItemView : RelativeLayout {
    @Bean
    lateinit var teachersService: TeachersService
    @Bean
    lateinit var studentsService: StudentsService

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    fun bind(studentPayment: StudentPayment): MonitoringTeacherPaymentsItemView {
        monitoringTeacherPaymentsItemAmountView.text = "${studentPayment.amount} Р"

        monitoringTeacherPaymentsItemTeacherView.text = teachersService.getTeacherById(
                studentPayment.teacherId
        )?.name ?: "?"

        monitoringTeacherPaymentsItemStudentView.text = studentsService.getStudent(
                studentPayment.studentId
        )?.name ?: "?"

        return this
    }
}

class MonitoringTeacherPaymentsListAdapter(context: Context) : BaseItemsListAdapter<StudentPayment>(context) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return if (convertView == null) {
            MonitoringTeacherPaymentsItemView_.build(context)
        } else {
            convertView as MonitoringTeacherPaymentsItemView
        }.bind(getItem(position))
    }
}

@SuppressLint("Registered")
@EActivity(R.layout.activity_monitoring_teacher_payments)
open class MonitoringTeacherPaymentsActivity : BaseActivity() {
    companion object {
        const val EXTRA_TEACHER_ID = "EXTRA_TEACHER_ID"
    }

    @Extra(EXTRA_TEACHER_ID)
    @JvmField
    var teacherId = 0L

    @Bean
    lateinit var studentPaymentsService: StudentsPaymentsService

    @AfterViews
    fun init() {
        monitoringTeacherPaymentsHeaderView
                .setLeftButtonAction { doFinish() }

        monitoringTeacherPaymentsSecondaryHeaderView.bind(teacherId)

        val adapter = MonitoringTeacherPaymentsListAdapter(this)

        adapter.setItems(
                studentPaymentsService.getPaymentsToTeacher(teacherId)
        )

        monitoringTeacherPaymentsListView.adapter = adapter
    }

    override fun onBackPressed() {
        doFinish()
    }

    private fun doFinish() {
        finish()
        overridePendingTransition(R.anim.slide_close_enter, R.anim.slide_close_exit)
    }
}
