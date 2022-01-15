package com.bdev.hengschoolteacher.ui.views.app.monitoring

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import com.bdev.hengschoolteacher.ui.activities.monitoring.MonitoringLessonsActivity
import com.bdev.hengschoolteacher.ui.activities.monitoring.MonitoringStudentsActivity
import com.bdev.hengschoolteacher.ui.activities.monitoring.MonitoringTeachersActivity
import kotlinx.android.synthetic.main.view_header_monitoring.view.*

class MonitoringHeaderView(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {
    init {
        View.inflate(context, R.layout.view_header_monitoring, this)
    }

    enum class Item {
        LESSONS, TEACHERS, STUDENTS;
    }

    fun bind(currentItem: Item, hasLessonsAlert: Boolean, hasTeachersAlert: Boolean, hasStudentsAlert: Boolean) {
        monitoringHeaderLessonsView.bind(
                active = currentItem == Item.LESSONS,
                hasAlert = hasLessonsAlert,
                clickAction = { MonitoringLessonsActivity.redirectToSibling(context as BaseActivity) }
        )

        monitoringHeaderTeachersView.bind(
                active = currentItem == Item.TEACHERS,
                hasAlert = hasTeachersAlert,
                clickAction = { MonitoringTeachersActivity.redirectToSibling(context as BaseActivity) }
        )

        monitoringHeaderStudentsView.bind(
            active = currentItem == Item.STUDENTS,
            hasAlert = hasStudentsAlert,
            clickAction = { MonitoringStudentsActivity.redirectToSibling(context as BaseActivity) }
        )
    }
}

