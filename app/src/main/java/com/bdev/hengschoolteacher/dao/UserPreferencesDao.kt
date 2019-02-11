package com.bdev.hengschoolteacher.dao

import android.content.Context
import org.androidannotations.annotations.EBean
import org.androidannotations.annotations.RootContext
import java.io.Serializable

class UserPreferencesModel(
        var login: String?,
        var password: String?
) : Serializable

@EBean(scope = EBean.Scope.Singleton)
open class UserPreferencesDao : CommonDao<UserPreferencesModel>() {
    @RootContext
    lateinit var rootContext: Context

    fun setUserPreferences(userPreferencesModel: UserPreferencesModel) {
        readCache()

        getValue().login = userPreferencesModel.login
        getValue().password = userPreferencesModel.password

        persist()
    }

    fun getUserPreferences() : UserPreferencesModel {
        readCache()

        return getValue()
    }

    override fun getContext(): Context { return rootContext }
    override fun getFileName(): String { return UserPreferencesDao::class.java.canonicalName }
    override fun newInstance(): UserPreferencesModel { return UserPreferencesModel(null, null) }
}
