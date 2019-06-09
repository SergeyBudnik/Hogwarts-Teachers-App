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
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.EViewGroup

@EViewGroup(R.layout.view_header_monitoring)
open class MonitoringHeaderView(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {
    private enum class Item(val id: Int) {
        LESSONS(1), SALARIES(2), PAYMENTS(3);

        companion object {
            fun findById(id: Int): Item {
                return values().find { it.id == id } ?: throw RuntimeException()
            }
        }
    }

    private val item: Item

    init {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.MonitoringHeaderView, 0, 0)

        try {
            item = Item.findById(ta.getInt(R.styleable.MonitoringHeaderView_monitoring_item, 1))
        } finally {
            ta.recycle()
        }
    }

    @AfterViews
    fun init() {
        monitoringHeaderLessonsView.setActive(item == Item.LESSONS)
        monitoringHeaderSalariesView.setActive(item == Item.SALARIES)
        monitoringHeaderPaymentsView.setActive(item == Item.PAYMENTS)

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

