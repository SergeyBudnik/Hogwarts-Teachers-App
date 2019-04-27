package com.bdev.hengschoolteacher.data.auth

import org.codehaus.jackson.annotate.JsonCreator
import org.codehaus.jackson.annotate.JsonProperty

class AuthInfo @JsonCreator constructor(
        @JsonProperty("token") val token: String
)
