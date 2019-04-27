package com.bdev.hengschoolteacher.data.school.teacher

import org.codehaus.jackson.annotate.JsonCreator
import org.codehaus.jackson.annotate.JsonProperty

class Teacher @JsonCreator constructor(
        @JsonProperty("id") val id: Long,
        @JsonProperty("login") val login: String,
        @JsonProperty("name") val name: String,
        @JsonProperty("type") val type: Type,
        @JsonProperty("phones") val phones: List<String>
) {
    enum class Type {
        NON_NATIVE, NATIVE
    }
}
