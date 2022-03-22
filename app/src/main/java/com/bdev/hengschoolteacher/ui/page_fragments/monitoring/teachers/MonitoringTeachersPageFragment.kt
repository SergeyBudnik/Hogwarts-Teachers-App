package com.bdev.hengschoolteacher.ui.page_fragments.monitoring.teachers

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.lifecycle.ViewModelProvider
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.data.school.staff.StaffMember
import com.bdev.hengschoolteacher.interactors.alerts.monitoring.AlertsMonitoringInteractor
import com.bdev.hengschoolteacher.interactors.alerts.monitoring.AlertsMonitoringTeachersInteractor
import com.bdev.hengschoolteacher.interactors.staff_members.StaffMembersStorageInteractor
import com.bdev.hengschoolteacher.ui.adapters.BaseItemsListAdapter
import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragment
import com.bdev.hengschoolteacher.ui.page_fragments.monitoring.teacher.lessons.MonitoringTeacherLessonsPageFragment
import com.bdev.hengschoolteacher.ui.utils.ViewVisibilityUtils.visibleElseGone
import com.bdev.hengschoolteacher.ui.views.app.AppLayoutView
import com.bdev.hengschoolteacher.ui.views.app.AppMenuView
import com.bdev.hengschoolteacher.ui.views.app.monitoring.MonitoringHeaderView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_monitoring_teachers.*
import kotlinx.android.synthetic.main.view_monitoring_teachers_item.view.*
import javax.inject.Inject

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

@AndroidEntryPoint
class MonitoringTeachersPageFragment : BasePageFragment<MonitoringTeachersPageFragmentViewModel>() {
    @Inject lateinit var staffMembersStorageInteractor: StaffMembersStorageInteractor
    @Inject lateinit var alertsMonitoringTeachersService: AlertsMonitoringTeachersInteractor
    @Inject lateinit var alertsMonitoringService: AlertsMonitoringInteractor

    override fun provideViewModel(): MonitoringTeachersPageFragmentViewModel =
        ViewModelProvider(this).get(MonitoringTeachersPageFragmentViewModelImpl::class.java)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.activity_monitoring_teachers, container, false)

    override fun doOnViewCreated() {
        super.doOnViewCreated()

        monitoringTeachersHeaderView
                .setLeftButtonAction { monitoringTeachersMenuLayoutView.openMenu() }

        monitoringTeachersSecondaryHeaderView.bind(
                currentItem = MonitoringHeaderView.Item.TEACHERS,
                hasTeachersAlert = alertsMonitoringService.teachersHaveAlerts(),
                hasLessonsAlert = alertsMonitoringService.lessonsHaveAlerts(),
                hasStudentsAlert = alertsMonitoringService.studentsHaveAlerts()
        )

        monitoringTeachersMenuLayoutView.setCurrentMenuItem(AppMenuView.Item.MONITORING)

        val adapter = MonitoringTeachersListAdapter(requireContext())

        adapter.setItems(staffMembersStorageInteractor.getAllStaffMembers().map {
            MonitoringTeachersItemViewModel(
                    staffMember = it,
                    hasAlerts = alertsMonitoringTeachersService.haveAlerts(it.login)
            )
        })

        monitoringTeachersListView.adapter = adapter

        monitoringTeachersListView.setOnItemClickListener { _, _, position, _ ->
            val teacher = adapter.getItem(position)

//            MonitoringTeacherLessonsPageFragment.redirectToChild(
//                    current = this,
//                    teacherLogin = teacher.staffMember.login
//            )
        }
    }

    override fun getAppLayoutView(): AppLayoutView? {
        return null
    }
}
