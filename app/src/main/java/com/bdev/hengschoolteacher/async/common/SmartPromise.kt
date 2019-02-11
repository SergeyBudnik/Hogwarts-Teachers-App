package com.bdev.hengschoolteacher.async.common

import com.bdev.hengschoolteacher.exceptions.http.HttpForbiddenException
import nl.komponents.kovenant.Promise

class SmartPromise<T, Exception>(private val promise: Promise<T, Exception>) {
    fun onSuccess(action: () -> Unit): SmartPromise<T, Exception> {
        promise.success { action.invoke() }

        return this
    }

    fun onAuthFail(action: () -> Unit): SmartPromise<T, Exception> {
        promise.fail {
            if (isForbiddenFail(it)) {
                action.invoke()
            }
        }

        return this
    }

    fun onOtherFail(action: () -> Unit): SmartPromise<T, Exception> {
        promise.fail {
            if (!isForbiddenFail(it)) {
                action.invoke()
            }
        }

        return this
    }

    private fun isForbiddenFail(e: Exception): Boolean {
        return e is HttpForbiddenException
    }
}
