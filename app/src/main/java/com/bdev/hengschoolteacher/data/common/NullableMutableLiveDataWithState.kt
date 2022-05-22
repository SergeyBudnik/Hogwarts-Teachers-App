package com.bdev.hengschoolteacher.data.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class NullableMutableLiveDataWithState<T : Any>(initialValue: T?) {
    private var state: T? = initialValue
    private val liveData = MutableLiveData<T>()

    init {
        initialValue?.let {
            liveData.value = it
        }
    }

    fun getLiveData(): LiveData<T> = liveData

    fun getValue(): T? = state

    fun updateValue(
        defaultValue: T,
        updateOnChangeOnly: Boolean = true,
        comparator: (T, T) -> Boolean = { o1, o2 -> o1 == o2 },
        mutator: (T) -> T
    ) {
        synchronized(this) {
            val oldState = state ?: defaultValue
            val newState = mutator(oldState)

            val update = if (updateOnChangeOnly) {
                !comparator(oldState, newState)
            } else {
                true
            }

            if (update) {
                state = newState

                liveData.postValue(newState)
            }
        }
    }

    fun updateValue(
        updateOnChangeOnly: Boolean = true,
        comparator: (T?, T) -> Boolean = { o1, o2 -> o1 == o2 },
        mutator: (T?) -> T
    ) {
        synchronized(this) {
            val oldState = state
            val newState = mutator(oldState)

            val update = if (updateOnChangeOnly) {
                !comparator(oldState, newState)
            } else {
                true
            }

            if (update) {
                state = newState

                liveData.postValue(newState)
            }
        }
    }
}