package com.bdev.hengschoolteacher.ui.activities.monitoring

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.data.school.teacher.Teacher
import com.bdev.hengschoolteacher.service.alerts.monitoring.AlertsMonitoringTeachersService
import com.bdev.hengschoolteacher.service.teacher.TeacherStorageService
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import com.bdev.hengschoolteacher.ui.activities.monitoring.teacher.MonitoringTeacherLessonsActivity
import com.bdev.hengschoolteacher.ui.adapters.BaseItemsListAdapter
import com.bdev.hengschoolteacher.ui.utils.RedirectBuilder
import com.bdev.hengschoolteacher.ui.views.app.AppMenuView
import com.bdev.hengschoolteacher.ui.views.app.monitoring.MonitoringHeaderView
import kotlinx.android.synthetic.main.activity_monitoring_teachers.*
import kotlinx.android.synthetic.main.view_monitoring_teachers_item.view.*
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EActivity
import org.androidannotations.annotations.EViewGroup

@EViewGroup(R.layout.view_monitoring_teachers_item)
open class MonitoringTeachersItemView : RelativeLayout {
    @Bean
    lateinit var alertsMonitoringTeachersService: AlertsMonitoringTeachersService

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    fun bind(teacher: Teacher): MonitoringTeachersItemView {
        monitoringTeachersItemNameView.text = teacher.name

        monitoringTeachersItemAlertView.visibility =
                if (alertsMonitoringTeachersService.haveAlerts(teacher.id)) {
                    View.VISIBLE
                } else {
                    View.GONE
                }

        return this
    }
}

class MonitoringTeachersListAdapter(context: Context) : BaseItemsListAdapter<Teacher>(context) {
    override fun getView(position: Int, convertView: View?, parentView: ViewGroup): View {
        return if (convertView == null) {
            MonitoringTeachersItemView_.build(context)
        } else {
            convertView as MonitoringTeachersItemView
        }.bind(getItem(position))
    }
}

@SuppressLint("Registered")
@EActivity(R.layout.activity_monitoring_teachers)
open class MonitoringTeachersActivity : BaseActivity() {
    companion object {
        fun redirectToSibling(current: BaseActivity) {
            RedirectBuilder
                    .redirect(current)
                    .to(MonitoringTeachersActivity_::class.java)
                    .goAndCloseCurrent()
        }
    }

    @Bean
    lateinit var teacherStorageService: TeacherStorageService

    @AfterViews
    fun init() {
        monitoringTeachersHeaderView
                .setLeftButtonAction { monitoringTeachersMenuLayoutView.openMenu() }

        monitoringTeachersSecondaryHeaderView.bind(currentItem = MonitoringHeaderView.Item.TEACHERS)

        monitoringTeachersMenuLayoutView.setCurrentMenuItem(AppMenuView.Item.MONITORING)

        val adapter = MonitoringTeachersListAdapter(this)

        adapter.setItems(teacherStorageService.getAllTeachers())

        monitoringTeachersListView.adapter = adapter

        monitoringTeachersListView.setOnItemClickListener { _, _, position, _ ->
            val teacher = adapter.getItem(position)

            MonitoringTeacherLessonsActivity.redirectToChild(
                    current = this,
                    teacherId = teacher.id
            )
        }
    }
}
