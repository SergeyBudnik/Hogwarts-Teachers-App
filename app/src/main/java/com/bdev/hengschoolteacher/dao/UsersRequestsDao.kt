package com.bdev.hengschoolteacher.dao

import com.bdev.hengschoolteacher.data.school.requests.UserRequest
import org.androidannotations.annotations.EBean
import org.codehaus.jackson.annotate.JsonCreator
import org.codehaus.jackson.annotate.JsonProperty
import org.codehaus.jackson.map.ObjectMapper

class UsersRequestModel @JsonCreator constructor(
        @JsonProperty("usersRequests") val usersRequests: List<UserRequest>
)

@EBean(scope = EBean.Scope.Singleton)
open class UsersRequestsDao : CommonDao<UsersRequestModel>() {
    override fun getFileName(): String {
        return "users-requests.data"
    }

    override fun newInstance(): UsersRequestModel {
        return UsersRequestModel(ArrayList())
    }

    override fun deserialize(json: String): UsersRequestModel {
        return ObjectMapper().readValue(json, UsersRequestModel::class.java)
    }
}
