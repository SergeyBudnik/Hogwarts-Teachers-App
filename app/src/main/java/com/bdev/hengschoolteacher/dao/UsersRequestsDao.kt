package com.bdev.hengschoolteacher.dao

import android.content.Context
import com.bdev.hengschoolteacher.data.school.requests.UserRequest
import org.androidannotations.annotations.EBean
import org.androidannotations.annotations.RootContext

class UsersRequestModel(
        var usersRequests: MutableList<UserRequest>
)

@EBean(scope = EBean.Scope.Singleton)
open class UsersRequestsDao : CommonDao<UsersRequestModel>() {
    @RootContext
    lateinit var contextValue: Context

    fun getUsersRequests(): List<UserRequest> {
        readCache()

        return getValue().usersRequests
    }

    fun setUsersRequests(usersRequests: List<UserRequest>) {
        readCache()

        getValue().usersRequests = usersRequests.toMutableList()

        persist()
    }

    override fun getContext(): Context {
        return contextValue
    }

    override fun getFileName(): String {
        return UsersRequestsDao::class.java.canonicalName
    }

    override fun newInstance(): UsersRequestModel {
        return UsersRequestModel(ArrayList())
    }
}
