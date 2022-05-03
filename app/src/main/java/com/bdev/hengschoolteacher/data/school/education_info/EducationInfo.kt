package com.bdev.hengschoolteacher.data.school.education_info

import com.google.gson.annotations.SerializedName

data class EducationInfo(
    @SerializedName("age") val age: EducationAge,
    @SerializedName("level") val level: EducationLevel
)