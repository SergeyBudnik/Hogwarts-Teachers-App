package com.bdev.hengschoolteacher.ui.activities.student

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.RelativeLayout
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.async.StudentsPaymentAsyncService
import com.bdev.hengschoolteacher.data.school.student.Student
import com.bdev.hengschoolteacher.data.school.student_payment.StudentPayment
import com.bdev.hengschoolteacher.data.school.student_payment.StudentPaymentInfo
import com.bdev.hengschoolteacher.service.GroupsService
import com.bdev.hengschoolteacher.service.LessonsService
import com.bdev.hengschoolteacher.service.StudentsPaymentsService
import com.bdev.hengschoolteacher.service.StudentsService
import com.bdev.hengschoolteacher.service.profile.ProfileService
import com.bdev.hengschoolteacher.service.teacher.TeacherStorageService
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import com.bdev.hengschoolteacher.ui.activities.teacher.TeacherActivity
import com.bdev.hengschoolteacher.ui.utils.KeyboardUtils
import com.bdev.hengschoolteacher.ui.utils.RedirectBuilder
import com.bdev.hengschoolteacher.ui.views.app.AppLayoutView
import com.bdev.hengschoolteacher.ui.views.app.student.StudentHeaderItem
import kotlinx.android.synthetic.main.activity_student_payment.*
import kotlinx.android.synthetic.main.view_student_payment_item.view.*
import org.androidannotations.annotations.*
import java.text.SimpleDateFormat
import java.util.*

@EViewGroup(R.layout.view_student_payment_item)
open class StudentPaymentItemView : RelativeLayout {
    @Bean
    lateinit var teacherStorageService: TeacherStorageService

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    fun bind(studentPayment: StudentPayment) {
        studentPaymentItemValueView.text = "${studentPayment.amount} Р"
        studentPaymentItemTimeView.text = SimpleDateFormat("dd.MM.yyyy", Locale.US).format(studentPayment.time)


        initTeachers(studentPayment)
    }

    private fun initTeachers(studentPayment: StudentPayment) {
        val teacher = teacherStorageService.getTeacherById(studentPayment.teacherId)

        studentPaymentItemTeacherView.text = teacher
                ?.name
                ?.split(" ")
                ?.map { it.substring(0, 1) }
                ?.fold("") { r, t -> r + t }
                ?: "?"

        if (teacher != null) {
            studentPaymentItemTeacherView.setOnClickListener {
                TeacherActivity.redirectToChild(
                        current = context as BaseActivity,
                        teacherId = teacher.id
                )
            }
        }
    }
}

@EBean
open class StudentPaymentsListAdapter : BaseAdapter() {
    @RootContext
    lateinit var context: Context

    private var payments: List<StudentPayment> = emptyList()

    fun setItems(payments: List<StudentPayment>) {
        this.payments = payments.sortedBy { -(it.id ?: 0) }
    }

    override fun getView(position: Int, convertView: View?, parentView: ViewGroup): View {
        val v = if (convertView == null) {
            StudentPaymentItemView_.build(context)
        } else {
            convertView as StudentPaymentItemView
        }

        v.bind(getItem(position))

        return v
    }

    override fun getItem(position: Int): StudentPayment {
        return payments[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return payments.size
    }
}

@SuppressLint("Registered")
@EActivity(R.layout.activity_student_payment)
open class StudentPaymentActivity : BaseActivity() {
    companion object {
        private const val EXTRA_STUDENT_ID = "EXTRA_STUDENT_ID"

        fun redirectToChild(current: BaseActivity, studentId: Long) {
            return redirect(current, studentId)
                    .withAnim(R.anim.slide_open_enter, R.anim.slide_open_exit)
                    .go()
        }

        fun redirectToSibling(current: BaseActivity, studentId: Long) {
            return redirect(current, studentId)
                    .withAnim(0, 0)
                    .goAndCloseCurrent()
        }

        private fun redirect(current: BaseActivity, studentId: Long): RedirectBuilder {
            return RedirectBuilder
                    .redirect(current)
                    .to(StudentPaymentActivity_::class.java)
                    .withExtra(EXTRA_STUDENT_ID, studentId)
        }
    }

    @Bean
    lateinit var groupsService: GroupsService
    @Bean
    lateinit var studentsService: StudentsService
    @Bean
    lateinit var lessonsService: LessonsService
    @Bean
    lateinit var studentPaymentsService: StudentsPaymentsService
    @Bean
    lateinit var teacherStorageService: TeacherStorageService
    @Bean
    lateinit var profileService: ProfileService

    @Bean
    lateinit var studentsPaymentAsyncService: StudentsPaymentAsyncService

    @Bean
    lateinit var studentPaymentsListAdapter: StudentPaymentsListAdapter

    @Extra(EXTRA_STUDENT_ID)
    @JvmField
    var studentId: Long = 0

    @AfterViews
    fun init() {
        val student = studentsService.getStudent(studentId) ?: throw RuntimeException()

        studentPaymentsHeaderView
                .setTitle("Студент. ${student.name}")
                .setLeftButtonAction { doFinish() }

        studentPaymentsSecondaryHeaderView.bind(
                item = StudentHeaderItem.PAYMENTS,
                studentId = studentId
        )

        studentPaymentAddPaymentView.setOnClickListener { addPayment(student) }

        studentPaymentsListAdapter.setItems(
                studentPaymentsService
                        .getPayments(studentId)
                        .sortedBy { it.time }
        )

        studentPaymentItemsListView.adapter = studentPaymentsListAdapter
    }

    private fun addPayment(student: Student) {
        KeyboardUtils.hideKeyboard(this)

        val me = profileService.getMe()
        val amount = studentPaymentAmountView.text.toString().toLong()
        val lessonStartTime = Date().time

        if (me != null) {
            studentsPaymentAsyncService
                    .addPayment(StudentPaymentInfo(
                            amount,
                            student.id,
                            me.id,
                            lessonStartTime,
                            false
                    ))
                    .onSuccess { runOnUiThread { onPaymentMarkSuccess() } }
                    .onAuthFail { println("AUTH") }
                    .onOtherFail { println("FAIL") }
        }
    }

    private fun onPaymentMarkSuccess() {
        doFinish()
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
