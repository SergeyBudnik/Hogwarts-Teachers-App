package com.bdev.hengschoolteacher.dao

import android.content.Context
import com.bdev.hengschoolteacher.data.auth.AuthInfo
import org.androidannotations.annotations.EBean
import org.androidannotations.annotations.RootContext
import java.io.Serializable

class AuthModel : Serializable {
    var authInfo: AuthInfo? = null
}

@EBean(scope = EBean.Scope.Singleton)
open class AuthDao : CommonDao<AuthModel>() {
    @RootContext
    lateinit var rootContext: Context

    fun getAuthInfo(): AuthInfo? {
        readCache()

        return getValue().authInfo
    }

    fun setAuthInfo(authInfo: AuthInfo?) {
        readCache()

        getValue().authInfo = authInfo

        persist()
    }

    override fun getContext(): Context { return rootContext }
    override fun getFileName(): String { return AuthDao::class.java.canonicalName }
    override fun newInstance(): AuthModel { return AuthModel() }
}
