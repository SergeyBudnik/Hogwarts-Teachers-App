package com.bdev.hengschoolteacher.data.school.teacher

import org.codehaus.jackson.annotate.JsonCreator
import org.codehaus.jackson.annotate.JsonProperty

class TeacherPayment @JsonCreator constructor(
        @JsonProperty("amount") val amount: Int,
        @JsonProperty("teacherAction") val teacherAction: TeacherAction
)
