package com.bdev.hengschoolteacher.ui.views.app.monitoring

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import com.bdev.hengschoolteacher.ui.activities.monitoring.MonitoringLessonsActivity_
import com.bdev.hengschoolteacher.ui.activities.monitoring.MonitoringStudentsActivity_
import com.bdev.hengschoolteacher.ui.activities.monitoring.MonitoringTeachersActivity_
import com.bdev.hengschoolteacher.ui.utils.RedirectBuilder.Companion.redirect
import kotlinx.android.synthetic.main.view_header_monitoring.view.*
import org.androidannotations.annotations.EViewGroup

@EViewGroup(R.layout.view_header_monitoring)
open class MonitoringHeaderView(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {
    enum class Item(val id: Int) {
        LESSONS(1), SALARIES(2), PAYMENTS(3);
    }

    fun bind(currentItem: Item) {
        monitoringHeaderLessonsView.setActive(currentItem == Item.LESSONS)
        monitoringHeaderSalariesView.setActive(currentItem == Item.SALARIES)
        monitoringHeaderPaymentsView.setActive(currentItem == Item.PAYMENTS)

        monitoringHeaderLessonsView.setOnClickListener {
            redirect(context as BaseActivity).to(MonitoringLessonsActivity_::class.java).goAndCloseCurrent()
        }

        monitoringHeaderSalariesView.setOnClickListener {
            redirect(context as BaseActivity).to(MonitoringTeachersActivity_::class.java).goAndCloseCurrent()
        }

        monitoringHeaderPaymentsView.setOnClickListener {
            redirect(context as BaseActivity).to(MonitoringStudentsActivity_::class.java).goAndCloseCurrent()
        }
    }
}

