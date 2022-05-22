package com.bdev.hengschoolteacher.data.auth

import com.google.gson.annotations.SerializedName

class AuthInfo constructor(
    @SerializedName("token") val token: String
)
