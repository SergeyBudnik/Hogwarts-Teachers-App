package com.bdev.hengschoolteacher.dao

import android.content.Context
import com.bdev.hengschoolteacher.data.school.staff.StaffMember
import dagger.hilt.android.qualifiers.ApplicationContext
import org.codehaus.jackson.annotate.JsonCreator
import org.codehaus.jackson.annotate.JsonProperty
import org.codehaus.jackson.map.ObjectMapper
import javax.inject.Inject

data class StaffMembersModel @JsonCreator constructor(
        @JsonProperty("staffMembers") val staffMembers: List<StaffMember>
)

interface StaffMembersDao : CommonDao<StaffMembersModel>

class StaffMembersDaoImpl @Inject constructor(
    @ApplicationContext context: Context
): StaffMembersDao, CommonDaoImpl<StaffMembersModel>(context = context) {
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
