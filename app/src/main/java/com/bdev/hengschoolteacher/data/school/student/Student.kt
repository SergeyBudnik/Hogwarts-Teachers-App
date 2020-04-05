package com.bdev.hengschoolteacher.data.school.student

import com.bdev.hengschoolteacher.data.school.education_info.EducationInfo
import com.bdev.hengschoolteacher.data.school.education_info.EducationLevel
import com.bdev.hengschoolteacher.data.school.person.Person
import org.codehaus.jackson.annotate.JsonCreator
import org.codehaus.jackson.annotate.JsonProperty

class Student @JsonCreator constructor(
        @JsonProperty("login") val login: String,
        @JsonProperty("person") val person: Person,
        @JsonProperty("educationInfo") val educationInfo: EducationInfo,
        @JsonProperty("statusType") val statusType: StudentStatusType,
        @JsonProperty("studentGroups") val studentGroups: List<StudentGroup>
)
