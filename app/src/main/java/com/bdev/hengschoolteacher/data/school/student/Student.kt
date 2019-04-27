package com.bdev.hengschoolteacher.data.school.student

import org.codehaus.jackson.annotate.JsonCreator
import org.codehaus.jackson.annotate.JsonProperty

class Student @JsonCreator constructor(
        @JsonProperty("id") val id: Long,
        @JsonProperty("statusType") val statusType: StudentStatusType,
        @JsonProperty("studentGroups") val studentGroups: List<StudentGroup>,
        @JsonProperty("name") val name: String,
        @JsonProperty("phones") val phones: List<String>,
        @JsonProperty("emails") val emails: List<String>,
        @JsonProperty("age") val age: StudentAge,
        @JsonProperty("educationLevel") val educationLevel: StudentEducationLevel
)
