package com.bdev.hengschoolteacher.dao

import com.bdev.hengschoolteacher.data.school.group.Group
import org.androidannotations.annotations.EBean
import org.codehaus.jackson.annotate.JsonCreator
import org.codehaus.jackson.annotate.JsonProperty
import org.codehaus.jackson.map.ObjectMapper

class GroupsModel @JsonCreator constructor(
    @JsonProperty("groups") val groups: List<Group>
)

@EBean(scope = EBean.Scope.Singleton)
open class GroupsDao : CommonDao<GroupsModel>() {
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
