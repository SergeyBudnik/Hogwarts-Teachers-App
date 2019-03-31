package com.bdev.hengschoolteacher.async

import com.bdev.hengschoolteacher.data.auth.AuthInfo
import com.bdev.hengschoolteacher.rest.configuration.RestConfiguration
import org.androidannotations.rest.spring.api.RestClientHeaders
import org.androidannotations.rest.spring.api.RestClientSupport
import org.apache.http.conn.ssl.SSLConnectionSocketFactory
import org.apache.http.conn.ssl.SSLContexts
import org.apache.http.impl.client.HttpClients
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.web.client.RestTemplate
import java.security.cert.X509Certificate

abstract class CommonAsyncService {
    fun authenticateAll(rests: List<RestClientHeaders>, authInfo: AuthInfo?) {
        val token = (authInfo ?: throw RuntimeException()).token

        rests.forEach { it.setHeader(RestConfiguration.HEADER_AUTHORIZATION, token) }
    }

    fun <T : RestClientSupport> trustSsl(restService: T): T {
        restService.restTemplate = getRestTemplate(
                restService.restTemplate.messageConverters
        )

        return restService
    }

    private fun getRestTemplate(converters: List<HttpMessageConverter<*>>): RestTemplate {
        val acceptingTrustStrategy = { _: Array<X509Certificate>, _: String -> true }

        val sslContext = SSLContexts.custom()
                .loadTrustMaterial(null, acceptingTrustStrategy)
                .build()

        val csf = SSLConnectionSocketFactory(sslContext)
        val httpClient = HttpClients.custom().setSSLSocketFactory(csf).build()
        val requestFactory = HttpComponentsClientHttpRequestFactory()

        requestFactory.httpClient = httpClient

        val restTemplate = RestTemplate(requestFactory)

        restTemplate.messageConverters = converters

        return restTemplate
    }
}
