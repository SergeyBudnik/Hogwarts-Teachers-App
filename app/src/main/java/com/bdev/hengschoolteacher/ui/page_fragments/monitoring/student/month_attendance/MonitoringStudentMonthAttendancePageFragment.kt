package com.bdev.hengschoolteacher.ui.page_fragments.monitoring.student.month_attendance

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.lifecycle.ViewModelProvider
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.data.school.Month
import com.bdev.hengschoolteacher.data.school.group.GroupType
import com.bdev.hengschoolteacher.data.school.student.StudentAttendance
import com.bdev.hengschoolteacher.data.school.student.StudentAttendanceType
import com.bdev.hengschoolteacher.interactors.students.StudentsStorageInteractor
import com.bdev.hengschoolteacher.interactors.students_attendances.StudentsAttendancesProviderInteractor
import com.bdev.hengschoolteacher.interactors.students_pricing.StudentsPricingInteractor
import com.bdev.hengschoolteacher.ui.adapters.BaseItemsListAdapter
import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragment
import com.bdev.hengschoolteacher.ui.resources.AppResources
import com.bdev.hengschoolteacher.ui.utils.TimeFormatUtils
import com.bdev.hengschoolteacher.ui.views.app.root.HtPageRootView
import com.bdev.hengschoolteacher.ui.views.app.monitoring.student.MonitoringStudentMonthHeaderView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_monitoring_student_month_attendance.*
import kotlinx.android.synthetic.main.view_monitoring_student_month_attendance_item.view.*
import javax.inject.Inject

class MonitoringStudentMonthAttendanceItemView : RelativeLayout {
    init {
        View.inflate(context, R.layout.view_monitoring_student_month_attendance_item, this)
    }

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    fun bind(studentAttendance: StudentAttendance, studentAttendancePrice: Int): MonitoringStudentMonthAttendanceItemView {
        monitoringStudentMonthAttendanceItemDateView.text = TimeFormatUtils.formatOnlyDate(
                studentAttendance.startTime
        )

        val startTimeString = TimeFormatUtils.formatOnlyTime(studentAttendance.startTime)
        val finishTimeString = TimeFormatUtils.formatOnlyTime(studentAttendance.finishTime)

        monitoringStudentMonthAttendanceItemTimeView.text = "$startTimeString - $finishTimeString"

        monitoringStudentMonthAttendanceItemGroupTypeView.text = when (studentAttendance.groupType) {
            GroupType.INDIVIDUAL -> "Индивидуальное"
            GroupType.GROUP -> "Групповое (${studentAttendance.studentsInGroup} чел)"
        }

        monitoringStudentMonthAttendanceItemAttendanceTypeView.text = getAttendanceTypeName(studentAttendance)

        monitoringStudentMonthAttendanceItemAttendanceTypeView.setTextColor(
                getAttendanceColor(studentAttendance)
        )

        monitoringStudentMonthAttendanceItemPriceView.text = "$studentAttendancePrice Р"

        return this
    }

    private fun getAttendanceTypeName(studentAttendance: StudentAttendance): String {
        return when (studentAttendance.type) {
            StudentAttendanceType.VISITED -> "Посещено"
            StudentAttendanceType.VALID_SKIP -> "Ув. пропуск"
            StudentAttendanceType.INVALID_SKIP -> "Неув. пропуск"
            StudentAttendanceType.FREE_LESSON -> "Беспл. урок"
        }
    }

    private fun getAttendanceColor(studentAttendance: StudentAttendance): Int {
        return AppResources.getColor(
                context = context,
                colorId = when (studentAttendance.type) {
                    StudentAttendanceType.VISITED -> R.color.fill_text_basic_positive
                    StudentAttendanceType.VALID_SKIP -> R.color.fill_text_basic_warning
                    StudentAttendanceType.INVALID_SKIP -> R.color.fill_text_basic_negative
                    StudentAttendanceType.FREE_LESSON -> R.color.fill_text_basic_action_link
                }
        )
    }
}

private class MonitoringStudentMonthAttendanceListAdapter(
        context: Context
) : BaseItemsListAdapter<Pair<StudentAttendance, Int>>(context) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return if (convertView == null) {
            MonitoringStudentMonthAttendanceItemView(context)
        } else {
            convertView as MonitoringStudentMonthAttendanceItemView
        }.bind(
                studentAttendance = getItem(position).first,
                studentAttendancePrice = getItem(position).second
        )
    }

    override fun setItems(items: List<Pair<StudentAttendance, Int>>) {
        super.setItems(items.sortedByDescending { it.first.startTime })
    }
}

@AndroidEntryPoint
class MonitoringStudentMonthAttendancePageFragment : BasePageFragment<MonitoringStudentMonthAttendancePageFragmentViewModel>() {
    companion object {
        const val EXTRA_STUDENT_LOGIN = "EXTRA_STUDENT_LOGIN"
        const val EXTRA_MONTH_INDEX = "EXTRA_MONTH_INDEX"
    }

    lateinit var studentLogin: String
    var monthIndex: Int = 0

    @Inject lateinit var studentsStorageInteractor: StudentsStorageInteractor
    @Inject lateinit var studentsAttendancesProviderInteractor: StudentsAttendancesProviderInteractor
    @Inject lateinit var studentsPricingInteractor: StudentsPricingInteractor

    override fun provideViewModel(): MonitoringStudentMonthAttendancePageFragmentViewModel =
        ViewModelProvider(this).get(MonitoringStudentMonthAttendancePageFragmentViewModelImpl::class.java)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.activity_monitoring_student_month_attendance, container, false)

    override fun doOnViewCreated() {
        super.doOnViewCreated()

        // studentLogin = intent.getStringExtra(EXTRA_STUDENT_LOGIN)!! todo
        // monthIndex = intent.getIntExtra(EXTRA_MONTH_INDEX, 0) todo

        val month = Month.findByIndex(monthIndex)

        monitoringStudentMonthAttendanceHeaderView
                .setTitle("Мониторинг. Студент. ${getString(month.nameId)}")
                .setLeftButtonAction { doFinish() }

        monitoringStudentMonthAttendanceSecondaryHeaderView.bind(
                studentLogin = studentLogin,
                monthIndex = monthIndex,
                item = MonitoringStudentMonthHeaderView.Item.ATTENDANCE
        )

        val student = studentsStorageInteractor.getByLogin(studentLogin)

        student?.let { monitoringStudentMonthAttendanceStudentView.bind(it) }

        fillList()
    }

    private fun fillList() {
        val adapter = MonitoringStudentMonthAttendanceListAdapter(requireContext())

        adapter.setItems(studentsAttendancesProviderInteractor.getMonthlyAttendances(
                studentLogin = studentLogin,
                month = monthIndex
        ).map {
            Pair(it, studentsPricingInteractor.getAttendancePrice(attendance = it))
        })

        monitoringStudentMonthAttendanceListView.adapter = adapter
    }

    private fun doFinish() {
//        finish()
//        overridePendingTransition(R.anim.slide_close_enter, R.anim.slide_close_exit)
    }
}
