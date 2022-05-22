package com.bdev.hengschoolteacher.data.school.staff

import com.bdev.hengschoolteacher.data.school.person.Person
import com.google.gson.annotations.SerializedName

data class StaffMember constructor(
    @SerializedName("login") val login: String,
    @SerializedName("person") val person: Person,
    @SerializedName("salaryIn30m") val salaryIn30m: Int
)
