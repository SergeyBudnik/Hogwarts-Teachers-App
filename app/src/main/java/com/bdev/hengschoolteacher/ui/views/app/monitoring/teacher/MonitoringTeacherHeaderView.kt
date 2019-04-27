package com.bdev.hengschoolteacher.ui.views.app.monitoring.teacher

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import com.bdev.hengschoolteacher.ui.activities.monitoring.MonitoringTeacherPaymentsActivity
import com.bdev.hengschoolteacher.ui.activities.monitoring.MonitoringTeacherPaymentsActivity_
import com.bdev.hengschoolteacher.ui.activities.monitoring.MonitoringTeacherSalaryActivity
import com.bdev.hengschoolteacher.ui.activities.monitoring.MonitoringTeacherSalaryActivity_
import com.bdev.hengschoolteacher.ui.utils.RedirectBuilder.Companion.redirect
import kotlinx.android.synthetic.main.view_monitoring_teacher_header.view.*
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.EViewGroup

@EViewGroup(R.layout.view_monitoring_teacher_header)
open class MonitoringTeacherHeaderView(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {
    private enum class Item(val id: Int) {
        SALARY(1), PAYMENTS(2);

        companion object {
            fun findById(id: Int): Item {
                return values().find { it.id == id } ?: throw RuntimeException()
            }
        }
    }

    private val item: Item

    private var teacherId: Long = 0

    init {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.MonitoringTeacherHeaderView, 0, 0)

        try {
            item = Item.findById(ta.getInt(R.styleable.MonitoringTeacherHeaderView_monitoring_teacher_item, 1))
        } finally {
            ta.recycle()
        }
    }

    @AfterViews
    fun init() {
        monitoringTeacherHeaderSalaryView.setActive(item == Item.SALARY)
        monitoringTeacherHeaderPaymentsView.setActive(item == Item.PAYMENTS)

        monitoringTeacherHeaderSalaryView.setOnClickListener {
            redirect(context as BaseActivity)
                    .to(MonitoringTeacherSalaryActivity_::class.java)
                    .withExtra(MonitoringTeacherSalaryActivity.EXTRA_TEACHER_ID, teacherId)
                    .goAndCloseCurrent()
        }

        monitoringTeacherHeaderPaymentsView.setOnClickListener {
            redirect(context as BaseActivity)
                    .to(MonitoringTeacherPaymentsActivity_::class.java)
                    .withExtra(MonitoringTeacherPaymentsActivity.EXTRA_TEACHER_ID, teacherId)
                    .goAndCloseCurrent()
        }
    }

    fun bind(teacherId: Long) {
        this.teacherId = teacherId
    }
 }
