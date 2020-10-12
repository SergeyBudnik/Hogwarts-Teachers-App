package com.bdev.hengschoolteacher.data.school.staff

import com.bdev.hengschoolteacher.data.school.person.Person
import org.codehaus.jackson.annotate.JsonCreator
import org.codehaus.jackson.annotate.JsonProperty

data class StaffMember @JsonCreator constructor(
        @JsonProperty("login") val login: String,
        @JsonProperty("person") val person: Person,
        @JsonProperty("salaryIn30m") val salaryIn30m: Int
)
