package com.bdev.hengschoolteacher.ui.page_fragments.monitoring_teacher.fragments.content.salary

import com.bdev.hengschoolteacher.data.common.NullableMutableLiveDataWithState
import com.bdev.hengschoolteacher.interactors.staff_members.StaffMembersStorageInteractor
import com.bdev.hengschoolteacher.interactors.teachers.TeacherSalaryInteractor
import com.bdev.hengschoolteacher.ui.page_fragments.monitoring_teacher.MonitoringTeacherPageFragmentArguments
import com.bdev.hengschoolteacher.ui.page_fragments.monitoring_teacher.data.MonitoringTeacherPageFragmentTab
import com.bdev.hengschoolteacher.ui.page_fragments.monitoring_teacher.fragments.content.MonitoringTeacherContentFragmentViewModel
import com.bdev.hengschoolteacher.ui.page_fragments.monitoring_teacher.fragments.content.MonitoringTeacherContentFragmentViewModelImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

interface MonitoringTeacherSalaryFragmentViewModel : MonitoringTeacherContentFragmentViewModel<
    MonitoringTeacherSalaryFragmentData
>

@HiltViewModel
class MonitoringTeacherSalaryFragmentViewModelImpl @Inject constructor(
    private val staffMembersStorageInteractor: StaffMembersStorageInteractor,
    private val teacherSalaryInteractor: TeacherSalaryInteractor
): MonitoringTeacherSalaryFragmentViewModel, MonitoringTeacherContentFragmentViewModelImpl<
    MonitoringTeacherSalaryFragmentData
>() {
    private lateinit var initialData: MonitoringTeacherSalaryFragmentData

    private val dataLiveData = NullableMutableLiveDataWithState<MonitoringTeacherSalaryFragmentData>(
        initialValue = null
    )

    override fun getDataLiveData() = dataLiveData.getLiveData()

    override fun isVisible() = dataLiveData.getValue()?.visible ?: false

    override fun setArgs(args: MonitoringTeacherPageFragmentArguments) {
        staffMembersStorageInteractor.getStaffMember(login = args.teacherLogin)?.let { teacher ->
            val payments = teacherSalaryInteractor.getTeacherPayments(
                teacherLogin = teacher.login,
                weekIndex = 0
            )

            initialData = MonitoringTeacherSalaryFragmentData(
                visible = false,
                calendarEnabled = false,
                teacher = teacher,
                payments = payments
            )

            dataLiveData.updateValue { initialData }
        }
    }

    override fun setCurrentTab(tab: MonitoringTeacherPageFragmentTab) {
        dataLiveData.updateValue(defaultValue = initialData) { oldValue ->
            oldValue.copy(visible = (tab == MonitoringTeacherPageFragmentTab.SALARY))
        }
    }
}