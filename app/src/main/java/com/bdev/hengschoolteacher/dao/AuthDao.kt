package com.bdev.hengschoolteacher.dao

import android.content.Context
import com.bdev.hengschoolteacher.data.auth.AuthInfo
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AuthModel constructor(
    @SerializedName("authInfo") val authInfo: AuthInfo?
)

interface AuthDao : CommonDao<AuthModel>

class AuthDaoImpl @Inject constructor(
    @ApplicationContext context: Context
) : AuthDao, CommonDaoImpl<AuthModel>(context = context) {
    override fun getFileName() = "auth.data"
    override fun newInstance(): AuthModel = AuthModel(null)
    override fun deserialize(json: String): AuthModel = Gson().fromJson(json, AuthModel::class.java)
}
