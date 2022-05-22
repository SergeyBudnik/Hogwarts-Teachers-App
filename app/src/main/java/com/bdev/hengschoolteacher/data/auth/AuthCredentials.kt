package com.bdev.hengschoolteacher.data.auth

import com.google.gson.annotations.SerializedName

class AuthCredentials constructor(
        @SerializedName("login") val username: String,
        @SerializedName("password") val password: String
)
