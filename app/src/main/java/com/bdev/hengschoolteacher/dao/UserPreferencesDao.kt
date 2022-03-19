package com.bdev.hengschoolteacher.dao

import org.androidannotations.annotations.EBean
import org.codehaus.jackson.annotate.JsonCreator
import org.codehaus.jackson.annotate.JsonProperty
import org.codehaus.jackson.map.ObjectMapper

class UserPreferencesModel @JsonCreator constructor(
        @JsonProperty("login") val login: String?,
        @JsonProperty("password") val password: String?
)

interface UserPreferencesDao : CommonDao<UserPreferencesModel>

@EBean(scope = EBean.Scope.Singleton)
open class UserPreferencesDaoImpl : UserPreferencesDao, CommonDaoImpl<UserPreferencesModel>() {
    override fun getFileName(): String {
        return "user-preferences.data"
    }

    override fun newInstance(): UserPreferencesModel {
        return UserPreferencesModel(null, null)
    }

    override fun deserialize(json: String): UserPreferencesModel {
        return ObjectMapper().readValue(json, UserPreferencesModel::class.java)
    }
}
