package com.bdev.hengschoolteacher.dao

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import org.codehaus.jackson.annotate.JsonCreator
import org.codehaus.jackson.annotate.JsonProperty
import org.codehaus.jackson.map.ObjectMapper
import javax.inject.Inject

class UserPreferencesModel @JsonCreator constructor(
        @JsonProperty("login") val login: String?,
        @JsonProperty("password") val password: String?
)

interface UserPreferencesDao : CommonDao<UserPreferencesModel>

class UserPreferencesDaoImpl @Inject constructor(
    @ApplicationContext context: Context
): UserPreferencesDao, CommonDaoImpl<UserPreferencesModel>(context = context) {
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
