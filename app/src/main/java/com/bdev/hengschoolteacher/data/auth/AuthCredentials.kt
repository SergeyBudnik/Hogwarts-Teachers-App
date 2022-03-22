package com.bdev.hengschoolteacher.data.auth

import org.codehaus.jackson.annotate.JsonCreator
import org.codehaus.jackson.annotate.JsonProperty

class AuthCredentials @JsonCreator constructor(
        @JsonProperty("login") val username: String,
        @JsonProperty("password") val password: String
)
