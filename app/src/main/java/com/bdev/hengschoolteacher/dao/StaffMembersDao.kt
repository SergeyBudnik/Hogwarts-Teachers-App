package com.bdev.hengschoolteacher.dao

import android.content.Context
import com.bdev.hengschoolteacher.data.school.staff.StaffMember
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

data class StaffMembersModel constructor(
    @SerializedName("staffMembers") val staffMembers: List<StaffMember>
)

interface StaffMembersDao : CommonDao<StaffMembersModel>

class StaffMembersDaoImpl @Inject constructor(
    @ApplicationContext context: Context
): StaffMembersDao, CommonDaoImpl<StaffMembersModel>(context = context) {
    override fun getFileName() = "staff-members.data"
    override fun newInstance(): StaffMembersModel = StaffMembersModel(emptyList())
    override fun deserialize(json: String): StaffMembersModel = Gson().fromJson(json, StaffMembersModel::class.java)
}
