package com.bdev.hengschoolteacher.data.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class MutableLiveDataWithState<T : Any>(initialValue: T) {
    private var state: T = initialValue
    private val liveData = MutableLiveData(initialValue)

    fun getLiveData(): LiveData<T> = liveData
    fun getValue(): T = state

    fun setValue(
        mutator: (T) -> T,
        updateOnChangeOnly: Boolean = true,
        comparator: (T, T) -> Boolean = { o1, o2 -> o1 == o2 }
    ) = updateValue(
        mutator = mutator,
        updateOnChangeOnly = updateOnChangeOnly,
        comparator = comparator,
        updateLiveDataAction = { liveData.value = it }
    )

    fun postValue(
        mutator: (T) -> T,
        updateOnChangeOnly: Boolean = true,
        comparator: (T, T) -> Boolean = { o1, o2 -> o1 == o2 }
    ) = updateValue(
        mutator = mutator,
        updateOnChangeOnly = updateOnChangeOnly,
        comparator = comparator,
        updateLiveDataAction = { liveData.postValue(it) }
    )

    private fun updateValue(
        mutator: (T) -> T,
        updateOnChangeOnly: Boolean,
        comparator: (T, T) -> Boolean,
        updateLiveDataAction: (T) -> Unit
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

                updateLiveDataAction(newState)
            }
        }
    }
}