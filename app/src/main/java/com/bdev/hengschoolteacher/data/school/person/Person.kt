package com.bdev.hengschoolteacher.data.school.person

import com.google.gson.annotations.SerializedName

data class Person constructor(
    @SerializedName("name") val name: String,
    @SerializedName("contacts") val contacts: PersonContacts
)

data class PersonContacts constructor(
    @SerializedName("phones") val phones: List<PersonContact>,
    @SerializedName("vkLinks") val vkLinks: List<PersonContact>
)

data class PersonContact constructor(
    @SerializedName("name") val name: String,
    @SerializedName("value") val value: String
)
