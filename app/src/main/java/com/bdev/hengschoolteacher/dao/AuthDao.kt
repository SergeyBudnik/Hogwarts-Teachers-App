package com.bdev.hengschoolteacher.dao

import android.content.Context
import com.bdev.hengschoolteacher.data.auth.AuthInfo
import dagger.hilt.android.qualifiers.ApplicationContext
import org.codehaus.jackson.annotate.JsonCreator
import org.codehaus.jackson.annotate.JsonProperty
import org.codehaus.jackson.map.ObjectMapper
import javax.inject.Inject

class AuthModel @JsonCreator constructor(
    @JsonProperty("authInfo") val authInfo: AuthInfo?
)

interface AuthDao : CommonDao<AuthModel>

class AuthDaoImpl @Inject constructor(
    @ApplicationContext context: Context
) : AuthDao, CommonDaoImpl<AuthModel>(context = context) {
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
