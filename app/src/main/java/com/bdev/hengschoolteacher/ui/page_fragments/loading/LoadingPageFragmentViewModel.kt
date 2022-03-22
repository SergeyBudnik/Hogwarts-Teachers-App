package com.bdev.hengschoolteacher.ui.page_fragments.loading

import androidx.lifecycle.LiveData
import com.bdev.hengschoolteacher.interactors.groups.GroupsLoadingInteractor
import com.bdev.hengschoolteacher.interactors.lessons_status.LessonsStatusLoadingInteractor
import com.bdev.hengschoolteacher.interactors.staff_members.StaffMembersLoadingInteractor
import com.bdev.hengschoolteacher.interactors.students.StudentsLoadingInteractor
import com.bdev.hengschoolteacher.interactors.students_attendances.StudentsAttendancesLoadingInteractor
import com.bdev.hengschoolteacher.interactors.students_payments.StudentsPaymentsLoadingInteractor
import com.bdev.hengschoolteacher.ui.events.EventsQueue
import com.bdev.hengschoolteacher.ui.events.EventsQueueLiveDataHolder
import com.bdev.hengschoolteacher.ui.navigation.NavCommand
import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragmentViewModel
import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragmentViewModelImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

interface LoadingPageFragmentViewModel : BasePageFragmentViewModel {
    fun getFailureEventsQueueLiveData(): LiveData<EventsQueue<Any>>

    fun load()
    fun proceedWithoutLoading()
}

@HiltViewModel
class LoadingPageFragmentViewModelImpl @Inject constructor(
    private val studentsLoadingInteractor: StudentsLoadingInteractor,
    private val studentsAttendancesLoadingInteractor: StudentsAttendancesLoadingInteractor,
    private val staffMembersLoadingInteractor: StaffMembersLoadingInteractor,
    private val lessonsStatusLoadingInteractor: LessonsStatusLoadingInteractor,
    private val studentsPaymentsLoadingInteractor: StudentsPaymentsLoadingInteractor,
    private val groupsLoadingInteractor: GroupsLoadingInteractor
): LoadingPageFragmentViewModel, BasePageFragmentViewModelImpl() {
    private val failureEventsQueueLiveDataHolder = EventsQueueLiveDataHolder<Any>()

    override fun getFailureEventsQueueLiveData(): LiveData<EventsQueue<Any>> =
        failureEventsQueueLiveDataHolder.getLiveData()

    override fun goBack() {
        // todo
    }

    override fun load() {
        val loadStudentsPromise = studentsLoadingInteractor.load()
        val loadLessonsStatusPromise = lessonsStatusLoadingInteractor.load()
        val loadGroupsPromise = groupsLoadingInteractor.load()
        val loadStudentsAttendancesPromise = studentsAttendancesLoadingInteractor.load()
        val loadStudentsPaymentsPromise = studentsPaymentsLoadingInteractor.load()
        val loadStaffMembersPromise = staffMembersLoadingInteractor.load()

        loadLessonsStatusPromise
            .and(loadStudentsPromise)
            .and(loadGroupsPromise)
            .and(loadStudentsAttendancesPromise)
            .and(loadStudentsPaymentsPromise)
            .and(loadStaffMembersPromise)
            .onSuccess { onSuccess() }
            .onAuthFail { /* ToDo: Go to relogin */ }
            .onOtherFail { failureEventsQueueLiveDataHolder.postEvent(true) /* ToDo: Event.any */ }
    }

    override fun proceedWithoutLoading() {
        /* ToDo: Go to profile lessons */
    }

    private fun onSuccess() {
        navigate(
            navCommand = NavCommand.forward(
                navDir = LoadingPageFragmentDirections.loadingToProfileLessons()
            )
        )
    }
}