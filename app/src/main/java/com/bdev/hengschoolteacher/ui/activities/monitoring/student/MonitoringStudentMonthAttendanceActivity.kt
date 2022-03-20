package com.bdev.hengschoolteacher.ui.activities.monitoring.student

import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.data.school.Month
import com.bdev.hengschoolteacher.data.school.group.GroupType
import com.bdev.hengschoolteacher.data.school.student.StudentAttendance
import com.bdev.hengschoolteacher.data.school.student.StudentAttendanceType
import com.bdev.hengschoolteacher.interactors.students.StudentsStorageInteractor
import com.bdev.hengschoolteacher.interactors.students_attendances.StudentsAttendancesProviderInteractor
import com.bdev.hengschoolteacher.interactors.students_pricing.StudentsPricingInteractor
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import com.bdev.hengschoolteacher.ui.adapters.BaseItemsListAdapter
import com.bdev.hengschoolteacher.ui.resources.AppResources
import com.bdev.hengschoolteacher.ui.utils.RedirectBuilder
import com.bdev.hengschoolteacher.ui.utils.TimeFormatUtils
import com.bdev.hengschoolteacher.ui.views.app.AppLayoutView
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
class MonitoringStudentMonthAttendanceActivity : BaseActivity() {
    companion object {
        const val EXTRA_STUDENT_LOGIN = "EXTRA_STUDENT_LOGIN"
        const val EXTRA_MONTH_INDEX = "EXTRA_MONTH_INDEX"

        fun redirectToChild(current: BaseActivity, studentLogin: String, monthIndex: Int) {
            redirect(current = current, studentLogin = studentLogin, monthIndex = monthIndex)
                    .withAnim(R.anim.slide_open_enter, R.anim.slide_open_exit)
                    .go()
        }

        fun redirectToSibling(current: BaseActivity, studentLogin: String, monthIndex: Int) {
            redirect(current = current, studentLogin = studentLogin, monthIndex = monthIndex)
                    .goAndCloseCurrent()
        }

        private fun redirect(current: BaseActivity, studentLogin: String, monthIndex: Int): RedirectBuilder {
            return RedirectBuilder
                    .redirect(current)
                    .to(MonitoringStudentMonthAttendanceActivity::class.java)
                    .withExtra(EXTRA_STUDENT_LOGIN, studentLogin)
                    .withExtra(EXTRA_MONTH_INDEX, monthIndex)
        }
    }

    lateinit var studentLogin: String
    var monthIndex: Int = 0

    @Inject lateinit var studentsStorageInteractor: StudentsStorageInteractor
    @Inject lateinit var studentsAttendancesProviderInteractor: StudentsAttendancesProviderInteractor
    @Inject lateinit var studentsPricingInteractor: StudentsPricingInteractor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_monitoring_student_month_attendance)

        studentLogin = intent.getStringExtra(EXTRA_STUDENT_LOGIN)!!
        monthIndex = intent.getIntExtra(EXTRA_MONTH_INDEX, 0)

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
        val adapter = MonitoringStudentMonthAttendanceListAdapter(this)

        adapter.setItems(studentsAttendancesProviderInteractor.getMonthlyAttendances(
                studentLogin = studentLogin,
                month = monthIndex
        ).map {
            Pair(it, studentsPricingInteractor.getAttendancePrice(attendance = it))
        })

        monitoringStudentMonthAttendanceListView.adapter = adapter
    }

    override fun onBackPressed() {
        doFinish()
    }

    private fun doFinish() {
        finish()
        overridePendingTransition(R.anim.slide_close_enter, R.anim.slide_close_exit)
    }

    override fun getAppLayoutView(): AppLayoutView? {
        return null
    }
}
