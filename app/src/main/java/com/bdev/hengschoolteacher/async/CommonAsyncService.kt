package com.bdev.hengschoolteacher.async

import com.bdev.hengschoolteacher.data.auth.AuthInfo
import com.bdev.hengschoolteacher.rest.configuration.RestConfiguration
import org.androidannotations.rest.spring.api.RestClientHeaders

abstract class CommonAsyncService {
    fun authenticateAll(rests: List<RestClientHeaders>, authInfo: AuthInfo?) {
        val token = (authInfo ?: throw RuntimeException()).token

        rests.forEach { it.setHeader(RestConfiguration.HEADER_AUTHORIZATION, token) }
    }
}
