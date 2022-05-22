package com.bdev.hengschoolteacher.data.school.student

import com.bdev.hengschoolteacher.data.school.education_info.EducationInfo
import com.bdev.hengschoolteacher.data.school.person.Person
import com.google.gson.annotations.SerializedName

class Student constructor(
    @SerializedName("login") val login: String,
    @SerializedName("person") val person: Person,
    @SerializedName("managerLogin") val managerLogin: String,
    @SerializedName("educationInfo") val educationInfo: EducationInfo,
    @SerializedName("statusType") val statusType: StudentStatusType,
    @SerializedName("studentGroups") val studentGroups: List<StudentGroup>
)
