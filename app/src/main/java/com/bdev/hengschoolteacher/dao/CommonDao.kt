package com.bdev.hengschoolteacher.dao

import android.content.Context
import android.util.Log
import com.bdev.hengschoolteacher.utils.StorageUtils
import org.androidannotations.annotations.EBean
import org.androidannotations.annotations.RootContext
import org.codehaus.jackson.map.ObjectMapper

import java.io.IOException
import java.util.concurrent.atomic.AtomicReference

@EBean
abstract class CommonDao<T> {
    @RootContext
    lateinit var context: Context

    private val cache = AtomicReference<T>(null)

    protected abstract fun getFileName(): String
    protected abstract fun newInstance(): T

    protected fun getValue(): T {
        return cache.get()
    }

    fun readValue(): T {
        readCache()

        return cache.get()
    }

    fun writeValue(o: T) {
        readCache()

        cache.set(o)

        persist()
    }

    private fun persist() {
        if (cache.get() != null) {
            try {
                StorageUtils.writeData(
                        context,
                        getFileName(),
                        ObjectMapper().writeValueAsString(cache.get())
                )
            } catch (e: IOException) {
                Log.e("CommonDao", "Persist failed", e)
            }
        }
    }

    private fun readCache() {
        if (cache.get() == null) {
            try {
                val json = StorageUtils.readData<Any>(
                        context,
                        getFileName()
                ) as String?

                if (json != null) {
                    cache.set(deserialize(json))
                }
            } catch (e: IOException) {
                Log.e("CommonDao", "Reading failed", e)
            }
        }

        if (cache.get() == null) {
            cache.set(newInstance())
        }
    }

    protected abstract fun deserialize(json: String): T
}