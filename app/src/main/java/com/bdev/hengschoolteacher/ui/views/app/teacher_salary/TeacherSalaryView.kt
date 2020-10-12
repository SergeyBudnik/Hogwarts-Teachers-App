package com.bdev.hengschoolteacher.ui.views.app.teacher_salary

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.data.school.DayOfWeek
import com.bdev.hengschoolteacher.data.school.teacher.TeacherActionType
import com.bdev.hengschoolteacher.data.school.teacher.TeacherPayment
import com.bdev.hengschoolteacher.service.teacher.TeacherSalaryService
import com.bdev.hengschoolteacher.ui.adapters.BaseWeekItemsListAdapter
import kotlinx.android.synthetic.main.view_teacher_salary.view.*
import kotlinx.android.synthetic.main.view_teacher_salary_item.view.*
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EViewGroup

@EViewGroup(R.layout.view_teacher_salary_item)
open class TeacherSalaryItemView : LinearLayout {
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
            TeacherSalaryItemView_.build(context)
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

@EViewGroup(R.layout.view_teacher_salary)
open class TeacherSalaryView : LinearLayout {
    @Bean
    lateinit var teacherSalaryService: TeacherSalaryService

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    fun init(teacherLogin: String, weekIndex: Int) {
        val teacherPayments = teacherSalaryService.getTeacherPayments(
                teacherLogin = teacherLogin,
                weekIndex = weekIndex
        )

        teacherSalaryWeekSumView.text = "${teacherPayments.fold(0) {v, tp -> v + tp.amount}} Р"

        val adapter = TeacherSalaryListAdapter(context)

        adapter.setItems(teacherPayments)

        teacherSalaryListView.adapter = adapter
    }
}
