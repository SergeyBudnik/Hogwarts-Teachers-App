package com.bdev.hengschoolteacher.dao

import android.content.Context
import com.bdev.hengschoolteacher.data.school.group.Group
import dagger.hilt.android.qualifiers.ApplicationContext
import org.codehaus.jackson.annotate.JsonCreator
import org.codehaus.jackson.annotate.JsonProperty
import org.codehaus.jackson.map.ObjectMapper
import javax.inject.Inject

class GroupsModel @JsonCreator constructor(
    @JsonProperty("groups") val groups: List<Group>
)

interface GroupsDao : CommonDao<GroupsModel>

class GroupsDaoImpl @Inject constructor(
    @ApplicationContext context: Context
) : GroupsDao, CommonDaoImpl<GroupsModel>(context = context) {
    override fun getFileName(): String {
        return "groups.data"
    }

    override fun newInstance(): GroupsModel {
        return GroupsModel(emptyList())
    }

    override fun deserialize(json: String): GroupsModel {
        return ObjectMapper().readValue(json, GroupsModel::class.java)
    }
}
