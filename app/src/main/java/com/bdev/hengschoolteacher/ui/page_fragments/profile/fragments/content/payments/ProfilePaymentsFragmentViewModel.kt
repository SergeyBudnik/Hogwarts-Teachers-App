package com.bdev.hengschoolteacher.ui.page_fragments.profile.fragments.content.payments

import com.bdev.hengschoolteacher.data.common.NullableMutableLiveDataWithState
import com.bdev.hengschoolteacher.interactors.profile.ProfileInteractor
import com.bdev.hengschoolteacher.interactors.staff_members.StaffMembersStorageInteractor
import com.bdev.hengschoolteacher.interactors.students.StudentsStorageInteractor
import com.bdev.hengschoolteacher.interactors.students_payments.StudentsPaymentsProviderInteractor
import com.bdev.hengschoolteacher.ui.page_fragments.profile.data.ProfilePageFragmentTab
import com.bdev.hengschoolteacher.ui.page_fragments.profile.fragments.content.ProfileContentFragmentViewModel
import com.bdev.hengschoolteacher.ui.page_fragments.profile.fragments.content.ProfileContentFragmentViewModelImpl
import com.bdev.hengschoolteacher.ui.views.app.payments.PaymentsItemViewData
import com.bdev.hengschoolteacher.ui.views.app.payments.PaymentsViewData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

interface ProfilePaymentsPageFragmentViewModel : ProfileContentFragmentViewModel<ProfilePaymentsFragmentData>

@HiltViewModel
class ProfilePaymentsPageFragmentViewModelImpl @Inject constructor(
    private val profileInteractor: ProfileInteractor,
    private val studentsPaymentsProviderInteractor: StudentsPaymentsProviderInteractor,
    private val studentsStorageInteractor: StudentsStorageInteractor,
    private val staffMembersStorageInteractor: StaffMembersStorageInteractor
): ProfilePaymentsPageFragmentViewModel, ProfileContentFragmentViewModelImpl<ProfilePaymentsFragmentData>() {
    private val dataLiveData = NullableMutableLiveDataWithState(
        initialValue = getInitialData()
    )

    override fun getDataLiveData() = dataLiveData.getLiveData()

    override fun isVisible() = dataLiveData.getValue()?.visible ?: false

    override fun setCurrentTab(tab: ProfilePageFragmentTab) {
        dataLiveData.updateValue(defaultValue = getInitialData()) { oldValue ->
            oldValue.copy(visible = (tab == ProfilePageFragmentTab.PAYMENTS))
        }
    }

    override fun onHeaderButton1Clicked() {
        dataLiveData.updateValue(defaultValue = getInitialData()) { oldValue ->
            oldValue.copy(filterEnabled = !oldValue.filterEnabled)
        }
    }

    private fun getInitialData(): ProfilePaymentsFragmentData =
        profileInteractor.getMe()?.let { me ->
            val allPayments = studentsPaymentsProviderInteractor.getForTeacher(
                teacherLogin = me.login,
                onlyUnprocessed = false
            )

            val filterEnabled = false

            val filteredPayments = allPayments.filter { payment ->
                !filterEnabled || !payment.processed
            }

            ProfilePaymentsFragmentData(
                visible = false,
                filterEnabled = false,

                allPayments = allPayments,
                filteredPayments = filteredPayments,

                paymentsViewData = PaymentsViewData(
                    paymentItemsData = filteredPayments
                        .sortedByDescending { payment ->
                            payment.info.time
                        }.map { studentPayment ->
                            PaymentsItemViewData(
                                studentPayment = studentPayment,
                                studentName = studentsStorageInteractor.getByLogin(
                                    studentPayment.info.studentLogin
                                )?.person?.name ?: "?",
                                staffMemberName = staffMembersStorageInteractor.getStaffMember(
                                    studentPayment.info.staffMemberLogin
                                )?.person?.name ?: "?",
                                singleTeacher = false,
                                clickAction = null
                            )
                        }
                )
            )
        } ?: throw RuntimeException()
}