package com.bdev.hengschoolteacher.data.school.student

import com.bdev.hengschoolteacher.data.school.group.GroupType
import org.codehaus.jackson.annotate.JsonCreator
import org.codehaus.jackson.annotate.JsonProperty

class StudentAttendance @JsonCreator constructor(
        @JsonProperty("studentLogin") val studentLogin: String,
        @JsonProperty("groupType") val groupType: GroupType,
        @JsonProperty("studentsInGroup") val studentsInGroup: Int,
        @JsonProperty("startTime") val startTime: Long,
        @JsonProperty("finishTime") val finishTime: Long,
        @JsonProperty("type") val type: StudentAttendanceType,
        @JsonProperty("ignoreSingleStudentPricing") val ignoreSingleStudentPricing: Boolean
)
