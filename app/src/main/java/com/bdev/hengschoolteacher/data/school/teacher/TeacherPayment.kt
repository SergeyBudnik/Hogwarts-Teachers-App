package com.bdev.hengschoolteacher.data.school.teacher

import com.google.gson.annotations.SerializedName

class TeacherPayment constructor(
    @SerializedName("amount") val amount: Int,
    @SerializedName("teacherAction") val teacherAction: TeacherAction
)
