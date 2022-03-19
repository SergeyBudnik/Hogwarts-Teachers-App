package com.bdev.hengschoolteacher.ui.activities.monitoring

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.data.school.staff.StaffMember
import com.bdev.hengschoolteacher.interactors.alerts.monitoring.AlertsMonitoringInteractorImpl
import com.bdev.hengschoolteacher.interactors.alerts.monitoring.AlertsMonitoringTeachersInteractorImpl
import com.bdev.hengschoolteacher.interactors.staff.StaffMembersStorageServiceImpl
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import com.bdev.hengschoolteacher.ui.activities.monitoring.teacher.MonitoringTeacherLessonsActivity
import com.bdev.hengschoolteacher.ui.adapters.BaseItemsListAdapter
import com.bdev.hengschoolteacher.ui.utils.RedirectBuilder
import com.bdev.hengschoolteacher.ui.utils.ViewVisibilityUtils.visibleElseGone
import com.bdev.hengschoolteacher.ui.views.app.AppLayoutView
import com.bdev.hengschoolteacher.ui.views.app.AppMenuView
import com.bdev.hengschoolteacher.ui.views.app.monitoring.MonitoringHeaderView
import kotlinx.android.synthetic.main.activity_monitoring_teachers.*
import kotlinx.android.synthetic.main.view_monitoring_teachers_item.view.*
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EActivity

class MonitoringTeachersItemView : RelativeLayout {
    init {
        View.inflate(context, R.layout.view_monitoring_teachers_item, this)
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    fun bind(model: MonitoringTeachersItemViewModel): MonitoringTeachersItemView {
        monitoringTeachersItemNameView.text = model.staffMember.person.name

        monitoringTeachersItemAlertView.visibility = visibleElseGone(visible = model.hasAlerts)

        return this
    }
}

data class MonitoringTeachersItemViewModel(
        val staffMember: StaffMember,
        val hasAlerts: Boolean
)

class MonitoringTeachersListAdapter(context: Context) : BaseItemsListAdapter<MonitoringTeachersItemViewModel>(context) {
    override fun getView(position: Int, convertView: View?, parentView: ViewGroup): View {
        return if (convertView == null) {
            MonitoringTeachersItemView(context)
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
    lateinit var staffMembersStorageService: StaffMembersStorageServiceImpl
    @Bean
    lateinit var alertsMonitoringTeachersService: AlertsMonitoringTeachersInteractorImpl
    @Bean
    lateinit var alertsMonitoringService: AlertsMonitoringInteractorImpl

    @AfterViews
    fun init() {
        monitoringTeachersHeaderView
                .setLeftButtonAction { monitoringTeachersMenuLayoutView.openMenu() }

        monitoringTeachersSecondaryHeaderView.bind(
                currentItem = MonitoringHeaderView.Item.TEACHERS,
                hasTeachersAlert = alertsMonitoringService.teachersHaveAlerts(),
                hasLessonsAlert = alertsMonitoringService.lessonsHaveAlerts(),
                hasStudentsAlert = alertsMonitoringService.studentsHaveAlerts()
        )

        monitoringTeachersMenuLayoutView.setCurrentMenuItem(AppMenuView.Item.MONITORING)

        val adapter = MonitoringTeachersListAdapter(this)

        adapter.setItems(staffMembersStorageService.getAllStaffMembers().map {
            MonitoringTeachersItemViewModel(
                    staffMember = it,
                    hasAlerts = alertsMonitoringTeachersService.haveAlerts(it.login)
            )
        })

        monitoringTeachersListView.adapter = adapter

        monitoringTeachersListView.setOnItemClickListener { _, _, position, _ ->
            val teacher = adapter.getItem(position)

            MonitoringTeacherLessonsActivity.redirectToChild(
                    current = this,
                    teacherLogin = teacher.staffMember.login
            )
        }
    }

    override fun getAppLayoutView(): AppLayoutView? {
        return null
    }
}
