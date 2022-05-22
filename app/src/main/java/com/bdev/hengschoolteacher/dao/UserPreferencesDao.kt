package com.bdev.hengschoolteacher.dao

import android.content.Context
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class UserPreferencesModel constructor(
    @SerializedName("login") val login: String?,
    @SerializedName("password") val password: String?
)

interface UserPreferencesDao : CommonDao<UserPreferencesModel>

class UserPreferencesDaoImpl @Inject constructor(
    @ApplicationContext context: Context
): UserPreferencesDao, CommonDaoImpl<UserPreferencesModel>(context = context) {
    override fun getFileName() = "user-preferences.data"
    override fun newInstance(): UserPreferencesModel = UserPreferencesModel(null, null)
    override fun deserialize(json: String): UserPreferencesModel = Gson().fromJson(json, UserPreferencesModel::class.java)
}
