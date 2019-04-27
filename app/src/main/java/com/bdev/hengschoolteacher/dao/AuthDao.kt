package com.bdev.hengschoolteacher.dao

import com.bdev.hengschoolteacher.data.auth.AuthInfo
import org.androidannotations.annotations.EBean
import org.codehaus.jackson.annotate.JsonCreator
import org.codehaus.jackson.annotate.JsonProperty
import org.codehaus.jackson.map.ObjectMapper

class AuthModel @JsonCreator constructor(
    @JsonProperty("authInfo") val authInfo: AuthInfo?
)

@EBean(scope = EBean.Scope.Singleton)
open class AuthDao : CommonDao<AuthModel>() {
    override fun getFileName(): String {
        return "auth.data"
    }

    override fun newInstance(): AuthModel {
        return AuthModel(null)
    }

    override fun deserialize(json: String): AuthModel {
        return ObjectMapper().readValue(json, AuthModel::class.java)
    }
}
