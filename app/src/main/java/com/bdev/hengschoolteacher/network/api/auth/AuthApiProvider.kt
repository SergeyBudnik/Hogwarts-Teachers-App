package com.bdev.hengschoolteacher.network.api.auth

import com.bdev.hengschoolteacher.network.api_provider.AllApiProvider
import com.bdev.hengschoolteacher.network.api_provider.ApiProvider
import javax.inject.Inject

interface AuthApiProvider : ApiProvider<AuthApi>

class AuthApiProviderImpl @Inject constructor(
    private val allApiProvider: AllApiProvider
): AuthApiProvider {
    override fun provide() = allApiProvider.provideAuthApi()
}