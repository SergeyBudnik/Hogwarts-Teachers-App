package com.bdev.hengschoolteacher.ui.views.app.monitoring

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragment
import com.bdev.hengschoolteacher.ui.page_fragments.monitoring.students.MonitoringStudentsPageFragment
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
                clickAction = {
                    //MonitoringLessonsPageFragment.redirectToSibling(context as BasePageFragment)
                }
        )

        monitoringHeaderTeachersView.bind(
                active = currentItem == Item.TEACHERS,
                hasAlert = hasTeachersAlert,
                clickAction = {
                    //MonitoringTeachersPageFragment.redirectToSibling(context as BasePageFragment)
                }
        )

        monitoringHeaderStudentsView.bind(
            active = currentItem == Item.STUDENTS,
            hasAlert = hasStudentsAlert,
            clickAction = {
                //MonitoringStudentsPageFragment.redirectToSibling(context as BasePageFragment)
            }
        )
    }
}

