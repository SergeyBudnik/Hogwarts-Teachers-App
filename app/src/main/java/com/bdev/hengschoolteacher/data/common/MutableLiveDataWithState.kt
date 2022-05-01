package com.bdev.hengschoolteacher.data.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class MutableLiveDataWithState<T : Any>(initialValue: T) {
    private var state: T = initialValue
    private val liveData = MutableLiveData(initialValue)

    fun getLiveData(): LiveData<T> = liveData

    fun updateValue(
        updateOnChangeOnly: Boolean = true,
        comparator: (T, T) -> Boolean = { o1, o2 -> o1 == o2 },
        mutator: (T) -> T
    ) {
        synchronized(this) {
            val oldState = state
            val newState = mutator(state)

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