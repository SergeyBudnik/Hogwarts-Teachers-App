package com.bdev.hengschoolteacher.data.school.education_info

import org.codehaus.jackson.annotate.JsonProperty

data class EducationInfo(
        @JsonProperty("age") val age: EducationAge,
        @JsonProperty("level") val level: EducationLevel
)