package com.bdev.hengschoolteacher.network.api.auth

import com.bdev.hengschoolteacher.network.api_provider.AllApiProviderImpl
import com.bdev.hengschoolteacher.network.api_provider.ApiProvider
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

interface AuthApiProvider : ApiProvider<AuthApi>

@EBean
open class AuthApiProviderImpl : AuthApiProvider {
    @Bean
    lateinit var allApiProvider: AllApiProviderImpl

    override fun provide() = allApiProvider.provideAuthApi()
}