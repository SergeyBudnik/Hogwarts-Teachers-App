package com.bdev.hengschoolteacher.dao

import com.bdev.hengschoolteacher.data.school.staff.StaffMember
import org.androidannotations.annotations.EBean
import org.codehaus.jackson.annotate.JsonCreator
import org.codehaus.jackson.annotate.JsonProperty
import org.codehaus.jackson.map.ObjectMapper

data class StaffMembersModel @JsonCreator constructor(
        @JsonProperty("staffMembers") val staffMembers: List<StaffMember>
)

@EBean(scope = EBean.Scope.Singleton)
open class StaffMembersDao : CommonDao<StaffMembersModel>() {
    override fun getFileName(): String {
        return "staff-members.data"
    }

    override fun newInstance(): StaffMembersModel {
        return StaffMembersModel(emptyList())
    }

    override fun deserialize(json: String): StaffMembersModel {
        return ObjectMapper().readValue(json, StaffMembersModel::class.java)
    }
}
