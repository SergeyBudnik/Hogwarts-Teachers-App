package com.bdev.hengschoolteacher.ui.activities.profile

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.data.school.DayOfWeek
import com.bdev.hengschoolteacher.data.school.teacher.TeacherActionType
import com.bdev.hengschoolteacher.data.school.teacher.TeacherPayment
import com.bdev.hengschoolteacher.service.*
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import com.bdev.hengschoolteacher.ui.adapters.BaseWeekItemsListAdapter
import com.bdev.hengschoolteacher.ui.views.app.AppMenuView
import kotlinx.android.synthetic.main.activity_profile_payment.*
import kotlinx.android.synthetic.main.view_profile_payment_item.view.*
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EActivity
import org.androidannotations.annotations.EViewGroup

@EViewGroup(R.layout.view_profile_payment_item)
open class ProfilePaymentItemView : LinearLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    fun bind(teacherPayment: TeacherPayment) {
        profilePaymentItemTypeView.text = when (teacherPayment.teacherAction.type) {
            TeacherActionType.ROAD -> "Дорога"
            TeacherActionType.LESSON -> "Занятие"
        }

        val startTime = resources.getString(teacherPayment.teacherAction.startTime.translationId)
        val finishTime = resources.getString(teacherPayment.teacherAction.finishTime.translationId)

        profilePaymentItemTimeView.text = "$startTime - $finishTime"

        profilePaymentItemAmountView.text = "${teacherPayment.amount} Р"
    }
}

class ProfilePaymentListAdapter(context: Context) : BaseWeekItemsListAdapter<TeacherPayment>(context) {
    override fun getElementView(item: TeacherPayment, convertView: View?): View {
        val v = if (convertView == null || convertView !is ProfilePaymentItemView) {
            ProfilePaymentItemView_.build(context)
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
        return object: Comparator<TeacherPayment> {
            override fun compare(tp1: TeacherPayment, tp2: TeacherPayment): Int {
                val dayComparision = tp1.teacherAction.dayOfWeek.compareTo(tp2.teacherAction.dayOfWeek)
                val startTimeComparision = tp1.teacherAction.startTime.order.compareTo(tp2.teacherAction.startTime.ordinal)
                val finishTimeComparision = tp1.teacherAction.finishTime.order.compareTo(tp2.teacherAction.finishTime.order)

                return when {
                    dayComparision != 0 -> dayComparision
                    startTimeComparision != 0 -> startTimeComparision
                    finishTimeComparision != 0 -> finishTimeComparision
                    else -> 0
                }
            }
        }
    }
}

@SuppressLint("Registered")
@EActivity(R.layout.activity_profile_payment)
open class ProfilePaymentActivity : BaseActivity() {
    @Bean
    lateinit var userPreferencesService: UserPreferencesService
    @Bean
    lateinit var teachersService: TeachersService
    @Bean
    lateinit var lessonStatusService: LessonStatusService
    @Bean
    lateinit var lessonsService: LessonsService
    @Bean
    lateinit var teachersPaymentService: TeachersPaymentService

    private var profilePaymentListAdapter = ProfilePaymentListAdapter(this)

    @AfterViews
    fun init() {
        profilePaymentHeaderView.setLeftButtonAction { profilePaymentMenuLayoutView.openMenu() }

        profilePaymentMenuLayoutView.setCurrentMenuItem(AppMenuView.Item.MY_PROFILE)

        val login = userPreferencesService.getUserLogin() ?: throw RuntimeException()

        val me = teachersService.getTeacherByLogin(login) ?: throw RuntimeException()

        val teacherPayments = teachersPaymentService.getTeacherPayments(me.id)

        profilePaymentThisWeekView.text = "${teacherPayments.fold(0) {v, tp -> v + tp.amount}} Р"

        profilePaymentListAdapter.setItems(teacherPayments)
        profilePaymentListView.adapter = profilePaymentListAdapter
    }
}
