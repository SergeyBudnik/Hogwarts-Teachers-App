package com.bdev.hengschoolteacher.async.common

import com.bdev.hengschoolteacher.exceptions.http.HttpForbiddenException
import nl.komponents.kovenant.task
import org.springframework.http.HttpStatus
import org.springframework.web.client.HttpClientErrorException

class SmartTask {
    companion object {
        fun <T> smartTask(action: () -> T): SmartPromise<T, Exception> {
            return SmartPromise(task {
                try {
                    return@task action.invoke()
                } catch (e: HttpClientErrorException) {
                    when (e.statusCode) {
                        HttpStatus.FORBIDDEN -> throw HttpForbiddenException()
                        else -> throw e
                    }
                }
            })
        }
    }
}
