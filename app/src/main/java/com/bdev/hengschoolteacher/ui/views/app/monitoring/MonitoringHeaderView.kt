package com.bdev.hengschoolteacher.ui.views.app.monitoring

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.service.StudentsPaymentsService
import com.bdev.hengschoolteacher.service.teacher.TeacherStorageService
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import com.bdev.hengschoolteacher.ui.activities.monitoring.MonitoringLessonsActivity
import com.bdev.hengschoolteacher.ui.activities.monitoring.MonitoringStudentsActivity
import com.bdev.hengschoolteacher.ui.activities.monitoring.MonitoringTeachersActivity
import kotlinx.android.synthetic.main.view_header_monitoring.view.*
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EViewGroup

@EViewGroup(R.layout.view_header_monitoring)
open class MonitoringHeaderView(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {
    @Bean
    lateinit var teacherStorageService: TeacherStorageService
    @Bean
    lateinit var studentsPaymentsService: StudentsPaymentsService

    enum class Item(val id: Int) {
        LESSONS(1), SALARIES(2), PAYMENTS(3);
    }

    fun bind(currentItem: Item) {
        monitoringHeaderLessonsView.setActive(currentItem == Item.LESSONS)
        monitoringHeaderSalariesView.setActive(currentItem == Item.SALARIES)
        monitoringHeaderPaymentsView.setActive(currentItem == Item.PAYMENTS)

        monitoringHeaderLessonsView.setOnClickListener {
            MonitoringLessonsActivity.redirectToSibling(context as BaseActivity)
        }

        monitoringHeaderSalariesView.setOnClickListener {
            MonitoringTeachersActivity.redirectToSibling(context as BaseActivity)
        }

        monitoringHeaderPaymentsView.setOnClickListener {
            MonitoringStudentsActivity.redirectToSibling(context as BaseActivity)
        }

        if (monitoringTeachersHasAlert()) {
            monitoringHeaderSalariesView.setIcon(
                    iconId = R.drawable.ic_alert,
                    colorId = R.color.fill_text_basic_negative
            )
        }
    }

    private fun monitoringTeachersHasAlert(): Boolean {
        return teacherStorageService
                .getAllTeachers()
                .map { !studentsPaymentsService.getPaymentsToTeacher(it.id, true).isEmpty() }
                .fold(false) { res, notEmpty -> res || notEmpty }
    }
}

