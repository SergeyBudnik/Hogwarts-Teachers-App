package com.bdev.hengschoolteacher.ui.activities.student

import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.RelativeLayout
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.data.school.staff.StaffMember
import com.bdev.hengschoolteacher.data.school.student.Student
import com.bdev.hengschoolteacher.data.school.student_payment.ExistingStudentPayment
import com.bdev.hengschoolteacher.data.school.student_payment.NewStudentPayment
import com.bdev.hengschoolteacher.data.school.student_payment.StudentPaymentInfo
import com.bdev.hengschoolteacher.interactors.groups.GroupsStorageInteractor
import com.bdev.hengschoolteacher.interactors.lessons.LessonsInteractor
import com.bdev.hengschoolteacher.interactors.profile.ProfileInteractor
import com.bdev.hengschoolteacher.interactors.staff_members.StaffMembersStorageInteractor
import com.bdev.hengschoolteacher.interactors.students.StudentsStorageInteractor
import com.bdev.hengschoolteacher.interactors.students_payments.StudentsPaymentsActionsInteractor
import com.bdev.hengschoolteacher.interactors.students_payments.StudentsPaymentsProviderInteractor
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import com.bdev.hengschoolteacher.ui.activities.teacher.TeacherActivity
import com.bdev.hengschoolteacher.ui.utils.KeyboardUtils
import com.bdev.hengschoolteacher.ui.utils.RedirectBuilder
import com.bdev.hengschoolteacher.ui.views.app.AppLayoutView
import com.bdev.hengschoolteacher.ui.views.app.student.StudentHeaderItem
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_student_payment.*
import kotlinx.android.synthetic.main.view_student_payment_item.view.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class StudentPaymentItemView : RelativeLayout {
    init {
        View.inflate(context, R.layout.view_student_payment_item, this)
    }

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    fun bind(existingStudentPayment: ExistingStudentPayment, teacher: StaffMember?) {
        studentPaymentItemValueView.text = "${existingStudentPayment.info.amount} Р"
        studentPaymentItemTimeView.text = SimpleDateFormat("dd.MM.yyyy", Locale.US)
                .format(existingStudentPayment.info.time)

        initTeachers(teacher = teacher)
    }

    private fun initTeachers(teacher: StaffMember?) {
        studentPaymentItemTeacherView.text = teacher
                ?.person?.name
                ?.split(" ")
                ?.map { it.substring(0, 1) }
                ?.fold("") { r, t -> r + t }
                ?: "?"

        if (teacher != null) {
            studentPaymentItemTeacherView.setOnClickListener {
                TeacherActivity.redirectToChild(
                        current = context as BaseActivity,
                        teacherLogin = teacher.login
                )
            }
        }
    }
}

class StudentPaymentsListAdapter(
    private val context: Context,
    private val staffMemberProvider: (String) -> StaffMember?
) : BaseAdapter() {
    private var paymentExistings: List<ExistingStudentPayment> = emptyList()

    fun setItems(paymentExistings: List<ExistingStudentPayment>) {
        this.paymentExistings = paymentExistings.sortedBy { -(it.id) }
    }

    override fun getView(position: Int, convertView: View?, parentView: ViewGroup): View {
        val v = if (convertView == null) {
            StudentPaymentItemView(context)
        } else {
            convertView as StudentPaymentItemView
        }

        getItem(position).let { payment ->
            v.bind(
                    teacher = staffMemberProvider(payment.info.staffMemberLogin),
                    existingStudentPayment = payment
            )
        }

        return v
    }

    override fun getItem(position: Int): ExistingStudentPayment {
        return paymentExistings[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return paymentExistings.size
    }
}

@AndroidEntryPoint
class StudentPaymentActivity : BaseActivity() {
    companion object {
        private const val EXTRA_STUDENT_LOGIN = "EXTRA_STUDENT_LOGIN"

        fun redirectToChild(current: BaseActivity, studentLogin: String) {
            return redirect(current, studentLogin)
                    .withAnim(R.anim.slide_open_enter, R.anim.slide_open_exit)
                    .go()
        }

        fun redirectToSibling(current: BaseActivity, studentLogin: String) {
            return redirect(current, studentLogin)
                    .withAnim(0, 0)
                    .goAndCloseCurrent()
        }

        private fun redirect(current: BaseActivity, studentLogin: String): RedirectBuilder {
            return RedirectBuilder
                    .redirect(current)
                    .to(StudentPaymentActivity::class.java)
                    .withExtra(EXTRA_STUDENT_LOGIN, studentLogin)
        }
    }

    @Inject lateinit var groupsStorageInteractor: GroupsStorageInteractor
    @Inject lateinit var studentsStorageInteractor: StudentsStorageInteractor
    @Inject lateinit var lessonsService: LessonsInteractor
    @Inject lateinit var studentsPaymentsProviderInteractor: StudentsPaymentsProviderInteractor
    @Inject lateinit var profileInteractor: ProfileInteractor
    @Inject lateinit var studentsPaymentAsyncService: StudentsPaymentsActionsInteractor
    @Inject lateinit var staffMembersStorageInteractor: StaffMembersStorageInteractor

    lateinit var studentLogin: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_student_payment)

        studentLogin = intent.getStringExtra(EXTRA_STUDENT_LOGIN)!!

        val student = studentsStorageInteractor.getByLogin(studentLogin) ?: throw RuntimeException()

        studentPaymentsHeaderView
                .setTitle("Студент. ${student.person.name}")
                .setLeftButtonAction { doFinish() }

        studentPaymentsSecondaryHeaderView.bind(
                item = StudentHeaderItem.PAYMENTS,
                studentLogin = studentLogin
        )

        studentPaymentAddPaymentView.setOnClickListener { addPayment(student) }

        val studentPaymentsListAdapter = StudentPaymentsListAdapter(
            context = this,
            staffMemberProvider = { staffMembersStorageInteractor.getStaffMember(it) }
        )

        studentPaymentsListAdapter.setItems(
                studentsPaymentsProviderInteractor
                        .getForStudent(studentLogin = studentLogin)
                        .sortedBy { it.info.time }
        )

        studentPaymentItemsListView.adapter = studentPaymentsListAdapter
    }

    private fun addPayment(student: Student) {
        KeyboardUtils.hideKeyboard(this)

        val me = profileInteractor.getMe()
        val amount = studentPaymentAmountView.text.toString().toLong()
        val lessonStartTime = Date().time

        if (me != null) {
            studentsPaymentAsyncService
                    .addPayment(NewStudentPayment(
                            info = StudentPaymentInfo(
                                    studentLogin = student.login,
                                    staffMemberLogin = me.login,
                                    amount = amount,
                                    time = lessonStartTime
                            )
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
