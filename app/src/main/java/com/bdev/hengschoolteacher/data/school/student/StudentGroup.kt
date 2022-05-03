package com.bdev.hengschoolteacher.data.school.student

import com.google.gson.annotations.SerializedName

class StudentGroup constructor(
    @SerializedName("groupId") val groupId: Long,
    @SerializedName("startTime") val startTime: Long,
    @SerializedName("finishTime") val finishTime: Long
)
