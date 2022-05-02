package com.bdev.hengschoolteacher.ui.fragments.monitoring.header.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.bdev.hengschoolteacher.NavGraphDirections
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.ui.fragments.monitoring.header.data.MonitoringHeaderFragmentItem
import com.bdev.hengschoolteacher.ui.navigation.NavCommand
import kotlinx.android.synthetic.main.view_header_monitoring.view.*

class MonitoringHeaderView(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {
    init {
        View.inflate(context, R.layout.view_header_monitoring, this)
    }

    fun bind(
        currentItem: MonitoringHeaderFragmentItem,
        hasLessonsAlert: Boolean,
        hasTeachersAlert: Boolean,
        hasStudentsAlert: Boolean,
        navCommandHandler: (NavCommand) -> Unit
    ) {
        monitoringHeaderLessonsView.bind(
            active = currentItem == MonitoringHeaderFragmentItem.LESSONS,
            hasAlert = hasLessonsAlert,
            clickAction = {
                navCommandHandler(
                    NavCommand.top(
                        navDir = NavGraphDirections.monitoringToMonitoringLessons()
                    )
                )
            }
        )

        monitoringHeaderTeachersView.bind(
            active = currentItem == MonitoringHeaderFragmentItem.TEACHERS,
            hasAlert = hasTeachersAlert,
            clickAction = {
                navCommandHandler(
                    NavCommand.top(
                        navDir = NavGraphDirections.monitoringToMonitoringTeachers()
                    )
                )
            }
        )

        monitoringHeaderStudentsView.bind(
            active = currentItem == MonitoringHeaderFragmentItem.STUDENTS,
            hasAlert = hasStudentsAlert,
            clickAction = {
                navCommandHandler(
                    NavCommand.top(
                        navDir = NavGraphDirections.monitoringToMonitoringStudents()
                    )
                )
            }
        )
    }
}

