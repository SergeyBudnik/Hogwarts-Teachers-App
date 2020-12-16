package com.bdev.hengschoolteacher.ui.activities.monitoring.student

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.data.school.Month
import com.bdev.hengschoolteacher.data.school.group.GroupType
import com.bdev.hengschoolteacher.data.school.student.StudentAttendance
import com.bdev.hengschoolteacher.data.school.student.StudentAttendanceType
import com.bdev.hengschoolteacher.services.students_pricing.StudentsPricingService
import com.bdev.hengschoolteacher.services.students_attendances.StudentsAttendancesProviderService
import com.bdev.hengschoolteacher.services.students.StudentsStorageService
import com.bdev.hengschoolteacher.services.students.StudentsStorageServiceImpl
import com.bdev.hengschoolteacher.services.students_pricing.StudentsPricingServiceImpl
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import com.bdev.hengschoolteacher.ui.adapters.BaseItemsListAdapter
import com.bdev.hengschoolteacher.ui.resources.AppResources
import com.bdev.hengschoolteacher.ui.utils.RedirectBuilder
import com.bdev.hengschoolteacher.ui.utils.TimeFormatUtils
import com.bdev.hengschoolteacher.ui.views.app.AppLayoutView
import com.bdev.hengschoolteacher.ui.views.app.monitoring.student.MonitoringStudentMonthHeaderView
import kotlinx.android.synthetic.main.activity_monitoring_student_month_attendance.*
import kotlinx.android.synthetic.main.view_monitoring_student_month_attendance_item.view.*
import org.androidannotations.annotations.*

@EViewGroup(R.layout.view_monitoring_student_month_attendance_item)
open class MonitoringStudentMonthAttendanceItemView : RelativeLayout {
    @Bean(StudentsPricingServiceImpl::class)
    lateinit var studentsPricingService: StudentsPricingService

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    fun bind(studentAttendance: StudentAttendance): MonitoringStudentMonthAttendanceItemView {
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

        monitoringStudentMonthAttendanceItemPriceView.text = "${studentsPricingService.getAttendancePrice(studentAttendance)} Р"

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
) : BaseItemsListAdapter<StudentAttendance>(context) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return if (convertView == null) {
            MonitoringStudentMonthAttendanceItemView_.build(context)
        } else {
            convertView as MonitoringStudentMonthAttendanceItemView
        }.bind(getItem(position))
    }

    override fun setItems(items: List<StudentAttendance>) {
        super.setItems(items.sortedByDescending { it.startTime })
    }
}

@SuppressLint("Registered")
@EActivity(R.layout.activity_monitoring_student_month_attendance)
open class MonitoringStudentMonthAttendanceActivity : BaseActivity() {
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
                    .to(MonitoringStudentMonthAttendanceActivity_::class.java)
                    .withExtra(EXTRA_STUDENT_LOGIN, studentLogin)
                    .withExtra(EXTRA_MONTH_INDEX, monthIndex)
        }
    }

    @Extra(EXTRA_STUDENT_LOGIN)
    lateinit var studentLogin: String

    @Extra(EXTRA_MONTH_INDEX)
    @JvmField
    var monthIndex: Int = 0

    @Bean(StudentsStorageServiceImpl::class)
    lateinit var studentsStorageService: StudentsStorageService
    @Bean
    lateinit var studentsAttendancesProviderService: StudentsAttendancesProviderService

    @AfterViews
    fun init() {
        val month = Month.findByIndex(monthIndex)

        monitoringStudentMonthAttendanceHeaderView
                .setTitle("Мониторинг. Студент. ${getString(month.nameId)}")
                .setLeftButtonAction { doFinish() }

        monitoringStudentMonthAttendanceSecondaryHeaderView.bind(
                studentLogin = studentLogin,
                monthIndex = monthIndex,
                item = MonitoringStudentMonthHeaderView.Item.ATTENDANCE
        )

        val student = studentsStorageService.getByLogin(studentLogin)

        student?.let { monitoringStudentMonthAttendanceStudentView.bind(it) }

        fillList()
    }

    private fun fillList() {
        val adapter = MonitoringStudentMonthAttendanceListAdapter(this)

        adapter.setItems(studentsAttendancesProviderService.getMonthlyAttendances(
                studentLogin = studentLogin,
                month = monthIndex
        ))

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
