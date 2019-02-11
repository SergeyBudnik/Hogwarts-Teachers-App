package com.bdev.hengschoolteacher.dao

import android.content.Context
import android.util.Log
import com.bdev.hengschoolteacher.utils.StorageUtils

import java.io.IOException
import java.util.concurrent.atomic.AtomicReference

abstract class CommonDao<out T> {
    private val cache = AtomicReference<T>(null)

    protected abstract fun getContext(): Context
    protected abstract fun getFileName(): String
    protected abstract fun newInstance(): T

    protected fun getValue(): T {
        return cache.get()
    }

    fun persist() {
        if (cache.get() != null) {
            try {
                StorageUtils.writeData(getContext(), getFileName(), cache.get())
            } catch (e: IOException) {
                Log.e("CommonDao", "Persist failed", e)
            }
        }
    }

    protected fun readCache() {
        if (cache.get() == null) {
            try {
                cache.set(StorageUtils.readData<Any>(getContext(), getFileName()) as T)
            } catch (e: IOException) {
                Log.e("CommonDao", "Reading failed", e)
            }
        }

        if (cache.get() == null) {
            cache.set(newInstance())
        }
    }

    protected fun inTransaction(action: () -> Unit) {
        readCache()

        action.invoke()

        persist()
    }
}