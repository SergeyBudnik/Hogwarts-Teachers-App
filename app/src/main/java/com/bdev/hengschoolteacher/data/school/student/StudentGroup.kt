package com.bdev.hengschoolteacher.data.school.student

import org.codehaus.jackson.annotate.JsonCreator
import org.codehaus.jackson.annotate.JsonProperty

class StudentGroup @JsonCreator constructor(
        @JsonProperty("groupId") val groupId: Long,
        @JsonProperty("startTime") val startTime: Long,
        @JsonProperty("finishTIme") val finishTime: Long?
)
