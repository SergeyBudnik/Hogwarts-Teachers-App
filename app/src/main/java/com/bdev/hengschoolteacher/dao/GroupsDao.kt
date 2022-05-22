package com.bdev.hengschoolteacher.dao

import android.content.Context
import com.bdev.hengschoolteacher.data.school.group.Group
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class GroupsModel constructor(
    @SerializedName("groups") val groups: List<Group>
)

interface GroupsDao : CommonDao<GroupsModel>

class GroupsDaoImpl @Inject constructor(
    @ApplicationContext context: Context
) : GroupsDao, CommonDaoImpl<GroupsModel>(context = context) {
    override fun getFileName() = "groups.data"
    override fun newInstance(): GroupsModel = GroupsModel(emptyList())
    override fun deserialize(json: String): GroupsModel = Gson().fromJson(json, GroupsModel::class.java)
}
