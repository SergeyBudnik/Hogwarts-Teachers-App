package com.bdev.hengschoolteacher.network.api.auth

import com.bdev.hengschoolteacher.data.auth.AuthCredentials
import com.bdev.hengschoolteacher.data.auth.AuthInfo
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("login")
    fun login(@Body authCredentials: AuthCredentials): Call<AuthInfo>
}