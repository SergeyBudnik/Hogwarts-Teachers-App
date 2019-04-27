package com.bdev.hengschoolteacher.async.common

import com.bdev.hengschoolteacher.exceptions.http.HttpForbiddenException
import nl.komponents.kovenant.Promise
import nl.komponents.kovenant.combine.Tuple2
import nl.komponents.kovenant.combine.and

class SmartPromise<X, Exception>(private val promise: Promise<X, Exception>) {
    fun <Y> and(smartPromise: SmartPromise<Y, Exception>): SmartPromise<Tuple2<X, Y>, Exception> {
        return SmartPromise(promise.and(smartPromise.promise))
    }

    fun onSuccess(action: () -> Unit): SmartPromise<X, Exception> {
        promise.success { action.invoke() }

        return this
    }

    fun onAuthFail(action: () -> Unit): SmartPromise<X, Exception> {
        promise.fail {
            if (isForbiddenFail(it)) {
                action.invoke()
            }
        }

        return this
    }

    fun onOtherFail(action: () -> Unit): SmartPromise<X, Exception> {
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
