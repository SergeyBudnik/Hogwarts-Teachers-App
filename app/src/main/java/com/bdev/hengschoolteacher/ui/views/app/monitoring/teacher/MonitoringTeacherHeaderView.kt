package com.bdev.hengschoolteacher.ui.views.app.monitoring.teacher

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.bdev.hengschoolteacher.R
import kotlinx.android.synthetic.main.view_monitoring_teacher_header.view.*

class MonitoringTeacherHeaderView(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {
    enum class Item {
        LESSONS, SALARY, PAYMENTS, DEBTS;
    }

    init {
        View.inflate(context, R.layout.view_monitoring_teacher_header, this)
    }

    fun bind(
            currentItem: Item,
            teacherLogin: String,
            hasLessonsAlert: Boolean,
            hasPaymentsAlert: Boolean,
            hasDebtsAlert: Boolean
    ) {
        monitoringTeacherHeaderLessonsView.bind(
            active = currentItem == Item.LESSONS,
            hasAlert = hasLessonsAlert,
            clickAction = {
//                MonitoringTeacherLessonsPageFragment.redirectToSibling(
//                        current = context as BasePageFragment,
//                        teacherLogin = teacherLogin
//                )
            }
        )

        monitoringTeacherHeaderSalaryView.bind(
            active = currentItem == Item.SALARY,
            hasAlert = false,
            clickAction = {
//                MonitoringTeacherSalaryPageFragment.redirectToSibling(
//                        current = context as BasePageFragment,
//                        teacherLogin = teacherLogin
//                )
            }
        )

        monitoringTeacherHeaderPaymentsView.bind(
            active = currentItem == Item.PAYMENTS,
            hasAlert = hasPaymentsAlert,
            clickAction = {
//                MonitoringTeacherPaymentsPageFragment.redirectToSibling(
//                        current = context as BasePageFragment,
//                        teacherLogin = teacherLogin
//                )
            }
        )

        monitoringTeacherHeaderDebtsView.bind(
                active = currentItem == Item.DEBTS,
                hasAlert = hasDebtsAlert,
                clickAction = {
//                    MonitoringTeacherDebtsPageFragment.redirectToSibling(
//                            current = context as BasePageFragment,
//                            teacherLogin = teacherLogin
//                    )
                }
        )
    }
 }
