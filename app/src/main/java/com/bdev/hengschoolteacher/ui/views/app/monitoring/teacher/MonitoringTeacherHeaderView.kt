package com.bdev.hengschoolteacher.ui.views.app.monitoring.teacher

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import com.bdev.hengschoolteacher.ui.activities.monitoring.teacher.MonitoringTeacherLessonsActivity
import com.bdev.hengschoolteacher.ui.activities.monitoring.teacher.MonitoringTeacherPaymentsActivity
import com.bdev.hengschoolteacher.ui.activities.monitoring.teacher.MonitoringTeacherSalaryActivity
import kotlinx.android.synthetic.main.view_monitoring_teacher_header.view.*
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.EViewGroup

@EViewGroup(R.layout.view_monitoring_teacher_header)
open class MonitoringTeacherHeaderView(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {
    private enum class Item(val id: Int) {
        LESSONS(1), SALARY(2), PAYMENTS(3);

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
        monitoringTeacherHeaderLessonsView.setActive(item == Item.LESSONS)
        monitoringTeacherHeaderSalaryView.setActive(item == Item.SALARY)
        monitoringTeacherHeaderPaymentsView.setActive(item == Item.PAYMENTS)

        monitoringTeacherHeaderLessonsView.setOnClickListener {
            MonitoringTeacherLessonsActivity.redirect(
                    current = context as BaseActivity,
                    teacherId = teacherId
            ).goAndCloseCurrent()
        }

        monitoringTeacherHeaderSalaryView.setOnClickListener {
            MonitoringTeacherSalaryActivity.redirect(
                    current = context as BaseActivity,
                    teacherId = teacherId
            ).goAndCloseCurrent()
        }

        monitoringTeacherHeaderPaymentsView.setOnClickListener {
            MonitoringTeacherPaymentsActivity.redirect(
                    current = context as BaseActivity,
                    teacherId = teacherId
            ).goAndCloseCurrent()
        }
    }

    fun bind(teacherId: Long) {
        this.teacherId = teacherId
    }
 }
