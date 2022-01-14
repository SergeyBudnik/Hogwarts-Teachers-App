package com.bdev.hengschoolteacher.ui.views.app.teacher_salary

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.data.school.DayOfWeek
import com.bdev.hengschoolteacher.data.school.staff.StaffMember
import com.bdev.hengschoolteacher.data.school.teacher.TeacherActionType
import com.bdev.hengschoolteacher.data.school.teacher.TeacherPayment
import com.bdev.hengschoolteacher.ui.adapters.BaseWeekItemsListAdapter
import kotlinx.android.synthetic.main.view_teacher_salary.view.*
import kotlinx.android.synthetic.main.view_teacher_salary_item.view.*

class TeacherSalaryItemView : LinearLayout {
    init {
        View.inflate(context, R.layout.view_teacher_salary_item, this)
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    fun bind(teacherPayment: TeacherPayment) {
        teacherSalaryItemTypeView.text = when (teacherPayment.teacherAction.type) {
            TeacherActionType.ROAD -> "Дорога"
            TeacherActionType.LESSON -> "Занятие"
            TeacherActionType.ONLINE_LESSON -> "О.Занятие"
        }

        val startTime = resources.getString(teacherPayment.teacherAction.startTime.translationId)
        val finishTime = resources.getString(teacherPayment.teacherAction.finishTime.translationId)

        teacherSalaryItemTimeView.text = "$startTime - $finishTime"

        teacherSalaryItemAmountView.text = "${teacherPayment.amount} Р"
    }
}

class TeacherSalaryListAdapter(context: Context) : BaseWeekItemsListAdapter<TeacherPayment>(context) {
    override fun getElementView(item: TeacherPayment, convertView: View?): View {
        val v = if (convertView == null || convertView !is TeacherSalaryItemView) {
            TeacherSalaryItemView(context)
        } else {
            convertView
        }

        v.bind(item)

        return v
    }

    override fun getElementDayOfWeek(item: TeacherPayment): DayOfWeek {
        return item.teacherAction.dayOfWeek
    }

    override fun getElementComparator(): Comparator<TeacherPayment> {
        return Comparator { tp1, tp2 ->
            val dayComparision = tp1.teacherAction.dayOfWeek.compareTo(tp2.teacherAction.dayOfWeek)
            val startTimeComparision = tp1.teacherAction.startTime.order.compareTo(tp2.teacherAction.startTime.ordinal)
            val finishTimeComparision = tp1.teacherAction.finishTime.order.compareTo(tp2.teacherAction.finishTime.order)

            when {
                dayComparision != 0 -> dayComparision
                startTimeComparision != 0 -> startTimeComparision
                finishTimeComparision != 0 -> finishTimeComparision
                else -> 0
            }
        }
    }
}

class TeacherSalaryView : LinearLayout {
    init {
        View.inflate(context, R.layout.view_teacher_salary, this)
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    fun init(teacher: StaffMember, teacherPayments: List<TeacherPayment>) {
        teacherSalarySalaryIn30mView.text = "${teacher?.salaryIn30m} Р"
        teacherSalaryWeekSumView.text = "${teacherPayments.fold(0) {v, tp -> v + tp.amount}} Р"

        val adapter = TeacherSalaryListAdapter(context)

        adapter.setItems(teacherPayments)

        teacherSalaryListView.adapter = adapter
    }
}
