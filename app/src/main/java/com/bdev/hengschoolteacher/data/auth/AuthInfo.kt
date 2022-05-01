package com.bdev.hengschoolteacher.data.auth

import com.google.gson.annotations.SerializedName
import org.codehaus.jackson.annotate.JsonCreator
import org.codehaus.jackson.annotate.JsonProperty

class AuthInfo constructor(
        @SerializedName("token") val token: String
)
