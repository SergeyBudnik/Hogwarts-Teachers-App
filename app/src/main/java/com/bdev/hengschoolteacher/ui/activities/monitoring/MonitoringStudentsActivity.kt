package com.bdev.hengschoolteacher.ui.activities.monitoring

import android.annotation.SuppressLint
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.services.students.StudentsStorageService
import com.bdev.hengschoolteacher.services.students.StudentsStorageServiceImpl
import com.bdev.hengschoolteacher.services.students_attendances.StudentsAttendancesProviderService
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import com.bdev.hengschoolteacher.ui.utils.RedirectBuilder
import com.bdev.hengschoolteacher.ui.views.app.AppLayoutView
import com.bdev.hengschoolteacher.ui.views.app.AppMenuView
import com.bdev.hengschoolteacher.ui.views.app.monitoring.MonitoringHeaderView
import kotlinx.android.synthetic.main.activity_monitoring_students.*
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EActivity

@SuppressLint("Registered")
@EActivity(R.layout.activity_monitoring_students)
open class MonitoringStudentsActivity : BaseActivity() {
    companion object {
        fun redirectToSibling(current: BaseActivity) {
            RedirectBuilder
                    .redirect(current)
                    .to(MonitoringStudentsActivity_::class.java)
                    .goAndCloseCurrent()
        }
    }

    @Bean(StudentsStorageServiceImpl::class)
    lateinit var studentsStorageService: StudentsStorageService
    @Bean
    lateinit var studentsAttendancesProviderService: StudentsAttendancesProviderService

    private var filterEnabled = true

    private var search = ""

    @AfterViews
    fun init() {
        monitoringPaymentsHeaderView
                .setLeftButtonAction { monitoringPaymentsMenuLayoutView.openMenu() }
                .setFirstRightButtonAction { monitoringPaymentsHeaderSearchView.show() }
                .setSecondRightButtonAction { toggleFilter() }
                .setSecondRightButtonActive(filterEnabled)

        monitoringPaymentsMenuLayoutView.setCurrentMenuItem(AppMenuView.Item.MONITORING)

        monitoringPaymentsSecondaryHeaderView.bind(
                currentItem = MonitoringHeaderView.Item.STUDENTS
        )

        monitoringPaymentsHeaderSearchView.addOnTextChangeListener { search ->
            this.search = search.trim()

            initList()
        }

        initList()
    }

    private fun initList() {
        monitoringPaymentsListView.bind(
                students = studentsStorageService.getAll(),
                searchQuery = search,
                withDebtsOnly = filterEnabled
        )
    }

    private fun toggleFilter() {
        filterEnabled = !filterEnabled

        monitoringPaymentsHeaderView.setSecondRightButtonActive(filterEnabled)

        initList()
    }

    override fun getAppLayoutView(): AppLayoutView? {
        return null
    }
}
