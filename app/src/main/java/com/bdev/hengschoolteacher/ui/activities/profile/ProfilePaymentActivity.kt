package com.bdev.hengschoolteacher.ui.activities.profile

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.data.school.teacher.TeacherActionType
import com.bdev.hengschoolteacher.data.school.teacher.TeacherPayment
import com.bdev.hengschoolteacher.service.*
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import com.bdev.hengschoolteacher.ui.views.app.AppMenuView
import kotlinx.android.synthetic.main.activity_profile_payment.*
import kotlinx.android.synthetic.main.view_profile_payment_item.view.*
import org.androidannotations.annotations.*

@EViewGroup(R.layout.view_profile_payment_item)
open class ProfilePaymentItemView : LinearLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    fun bind(newDay: Boolean, teacherPayment: TeacherPayment) {
        profilePaymentItemDayView.visibility = if (newDay) { View.VISIBLE } else { View.GONE }
        profilePaymentItemDayView.text = resources.getString(teacherPayment.teacherAction.dayOfWeek.shortNameId)

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

@EBean
open class ProfilePaymentListAdapter : BaseAdapter() {
    @RootContext
    lateinit var context: Context

    private var teacherPayments: List<TeacherPayment> = emptyList()

    fun setItems(teacherPayments: List<TeacherPayment>) {
        this.teacherPayments = teacherPayments
    }

    override fun getView(position: Int, convertView: View?, parentView: ViewGroup): View {
        val v = if (convertView == null) {
            ProfilePaymentItemView_.build(context)
        } else {
            convertView as ProfilePaymentItemView
        }

        val previousItem = if (position != 0) { getItem(position - 1) } else { null }
        val currentItem = getItem(position)

        v.bind(
                previousItem == null || (previousItem.teacherAction.dayOfWeek != currentItem.teacherAction.dayOfWeek),
                getItem(position)
        )

        return v
    }

    override fun getItem(position: Int): TeacherPayment {
        return teacherPayments[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return teacherPayments.size
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

    @Bean
    lateinit var profilePaymentListAdapter: ProfilePaymentListAdapter

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
