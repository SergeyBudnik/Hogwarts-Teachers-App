package com.bdev.hengschoolteacher.data.school.group

import org.codehaus.jackson.annotate.JsonCreator
import org.codehaus.jackson.annotate.JsonProperty

enum class GroupType {
    INDIVIDUAL, GROUP
}

class Group @JsonCreator constructor(
        @JsonProperty("id") val id: Long,
        @JsonProperty("cabinetId") val cabinetId: Long,
        @JsonProperty("type") val type: GroupType,
        @JsonProperty("lessons") val lessons: List<Lesson>,
        @JsonProperty("color") val color: String
)