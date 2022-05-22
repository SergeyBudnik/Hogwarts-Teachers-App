package com.bdev.hengschoolteacher.data.school.student

import com.bdev.hengschoolteacher.data.school.group.GroupType
import com.google.gson.annotations.SerializedName

class StudentAttendance constructor(
    @SerializedName("studentLogin") val studentLogin: String,
    @SerializedName("groupType") val groupType: GroupType,
    @SerializedName("studentsInGroup") val studentsInGroup: Int,
    @SerializedName("startTime") val startTime: Long,
    @SerializedName("finishTime") val finishTime: Long,
    @SerializedName("type") val type: StudentAttendanceType,
    @SerializedName("ignoreSingleStudentPricing") val ignoreSingleStudentPricing: Boolean
)
