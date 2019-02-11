package com.bdev.hengschoolteacher.data.school.requests

import org.codehaus.jackson.annotate.JsonCreator
import org.codehaus.jackson.annotate.JsonProperty

class UserRequest @JsonCreator constructor(
        @JsonProperty("id") val id: Long,
        @JsonProperty("name") val name: String,
        @JsonProperty("phone") val phone: String,
        @JsonProperty("date") val date: Long
)
