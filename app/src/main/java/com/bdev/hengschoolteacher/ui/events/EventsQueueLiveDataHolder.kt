package com.bdev.hengschoolteacher.ui.events

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class EventsQueueLiveDataHolder<T> {
    private val eventsQueue = EventsQueue<T>()
    private val eventsQueueLiveData = MutableLiveData<EventsQueue<T>>()

    fun getLiveData(): LiveData<EventsQueue<T>> = eventsQueueLiveData

    fun postEvent(event: T) {
        eventsQueue.addEvent(event)

        eventsQueueLiveData.postValue(eventsQueue)
    }
}