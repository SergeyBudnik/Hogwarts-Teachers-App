package com.bdev.hengschoolteacher.data.school.group

import com.google.gson.annotations.SerializedName

enum class GroupType {
    INDIVIDUAL, GROUP
}

class Group constructor(
    @SerializedName("id") val id: Long,
    @SerializedName("type") val type: GroupType,
    @SerializedName("lessons") val lessons: List<Lesson>,
    @SerializedName("color") val color: String
)