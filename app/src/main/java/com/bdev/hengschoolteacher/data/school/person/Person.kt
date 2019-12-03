package com.bdev.hengschoolteacher.data.school.person

import org.codehaus.jackson.annotate.JsonCreator
import org.codehaus.jackson.annotate.JsonProperty

data class Person @JsonCreator constructor(
        @JsonProperty("name") val name: String,
        @JsonProperty("contacts") val contacts: PersonContacts
)

data class PersonContacts @JsonCreator constructor(
        @JsonProperty("phones") val phones: List<PersonContact>,
        @JsonProperty("vkLinks") val vkLinks: List<PersonContact>
)

data class PersonContact @JsonCreator constructor(
        @JsonProperty("name") val name: String,
        @JsonProperty("value") val value: String
)
