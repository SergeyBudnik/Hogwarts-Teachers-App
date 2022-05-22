package com.bdev.hengschoolteacher.ui.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bdev.hengschoolteacher.ui.navigation.NavCommand
import com.bdev.hengschoolteacher.ui.activities.HTActivityViewModel
import com.bdev.hengschoolteacher.ui.events.EventsQueue
import com.bdev.hengschoolteacher.ui.events.EventsQueueLiveDataHolder
import io.reactivex.disposables.Disposable

interface BaseFragmentViewModel {
    fun getNavCommandEventLiveData(): LiveData<EventsQueue<NavCommand>>

    fun init(activityViewModel: HTActivityViewModel)

    fun addDisposable(disposable: Disposable)

    fun onCreate(fragment: BaseFragment<*>)
    fun onStart(fragment: BaseFragment<*>)
    fun onResume(fragment: BaseFragment<*>)
    fun onPause(fragment: BaseFragment<*>)
    fun onStop(fragment: BaseFragment<*>)

    fun navigate(navCommand: NavCommand)
}

abstract class BaseFragmentViewModelImpl : BaseFragmentViewModel, ViewModel() {
    protected lateinit var activityViewModel: HTActivityViewModel private set

    private val disposables = ArrayList<Disposable>()

    private val navCommandEventDataHolder = EventsQueueLiveDataHolder<NavCommand>()

    override fun getNavCommandEventLiveData() = navCommandEventDataHolder.getLiveData()

    override fun init(activityViewModel: HTActivityViewModel) {
        this.activityViewModel = activityViewModel
    }

    override fun onCreate(fragment: BaseFragment<*>) { /* Do nothing */ }
    override fun onStart(fragment: BaseFragment<*>) { /* Do nothing */ }
    override fun onResume(fragment: BaseFragment<*>) { /* Do nothing */ }
    override fun onPause(fragment: BaseFragment<*>) { /* Do nothing */ }
    override fun onStop(fragment: BaseFragment<*>) { /* Do nothing */ }

    override fun onCleared() {
        super.onCleared()

        disposables.filter { !it.isDisposed }.forEach { it.dispose() }
        disposables.clear()
    }

    override fun navigate(navCommand: NavCommand) {
        navCommandEventDataHolder.postEvent(navCommand)
    }

    final override fun addDisposable(disposable: Disposable) {
        disposables.add(disposable)
    }
}