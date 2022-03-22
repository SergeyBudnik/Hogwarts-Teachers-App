package com.bdev.hengschoolteacher.ui.activities

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bdev.hengschoolteacher.NavGraphDirections
import com.bdev.hengschoolteacher.interactors.auth.AuthStorageInteractor
import com.bdev.hengschoolteacher.ui.events.EventsQueue
import com.bdev.hengschoolteacher.ui.events.EventsQueueLiveDataHolder
import com.bdev.hengschoolteacher.ui.navigation.NavCommand
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

interface HTActivityViewModel {
    fun getNavCommandEventsQueueLiveData(): LiveData<EventsQueue<NavCommand>>

    fun navigate(navCommand: NavCommand)

    fun onCreate()
}

@HiltViewModel
class HTActivityViewModelImpl @Inject constructor(
    private val authStorageInteractor: AuthStorageInteractor
): HTActivityViewModel, ViewModel() {
    private val navCommandEventsQueueLiveDataHolder = EventsQueueLiveDataHolder<NavCommand>()

    override fun getNavCommandEventsQueueLiveData() = navCommandEventsQueueLiveDataHolder.getLiveData()

    override fun navigate(navCommand: NavCommand) {
        navCommandEventsQueueLiveDataHolder.postEvent(event = navCommand)
    }

    override fun onCreate() {
        val authInfo = authStorageInteractor.getAuthInfo()

        // appUpdateService.enqueueUpdate()

        navigate(navCommand = NavCommand.top(
            if (authInfo == null) {
                NavGraphDirections.startWithLoginGlobalNavAction()
            } else {
                NavGraphDirections.startWithLoadingGlobalNavAction()
            }
        ))
    }
}