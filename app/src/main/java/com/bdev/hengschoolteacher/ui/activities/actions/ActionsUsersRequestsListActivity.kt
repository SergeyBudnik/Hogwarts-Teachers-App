package com.bdev.hengschoolteacher.ui.activities.actions

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.data.school.requests.UserRequest
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.EActivity
import org.androidannotations.annotations.EBean
import org.androidannotations.annotations.EViewGroup

@EViewGroup(R.layout.view_actions_users_requests_item)
open class ActionsUsersRequestsItemView : LinearLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    fun bind(userRequest: UserRequest) {

    }
}

@EBean
open class ActionsUsersRequestsListAdapter : BaseAdapter() {
    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItem(p0: Int): Any {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemId(p0: Int): Long {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getCount(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

@SuppressLint("Registered")
@EActivity(R.layout.activity_actions_users_requests_list)
open class ActionsUsersRequestsListActivity : BaseActivity() {
    @AfterViews
    fun init() {

    }
}
