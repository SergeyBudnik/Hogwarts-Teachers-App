package com.bdev.hengschoolteacher.ui.activities.actions

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.data.school.requests.UserRequest
import com.bdev.hengschoolteacher.service.UsersRequestsService
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import com.bdev.hengschoolteacher.ui.activities.user_request.UserRequestActivity
import com.bdev.hengschoolteacher.ui.activities.user_request.UserRequestActivity_
import com.bdev.hengschoolteacher.ui.adapters.BaseItemsListAdapter
import com.bdev.hengschoolteacher.ui.utils.RedirectBuilder.Companion.redirect
import com.bdev.hengschoolteacher.ui.views.app.AppMenuView
import kotlinx.android.synthetic.main.activity_actions_users_requests_list.*
import kotlinx.android.synthetic.main.view_actions_users_requests_item.view.*
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EActivity
import org.androidannotations.annotations.EViewGroup

@EViewGroup(R.layout.view_actions_users_requests_item)
open class ActionsUsersRequestsItemView : LinearLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    fun bind(userRequest: UserRequest) {
        actionsUsersRequestsItemNameView.text = userRequest.name
    }
}

open class ActionsUsersRequestsListAdapter(context: Context) : BaseItemsListAdapter<UserRequest>(context) {
    override fun getView(position: Int, convertView: View?, parentView: ViewGroup?): View {
        val view = if (convertView == null) {
            ActionsUsersRequestsItemView_.build(context)
        } else {
            convertView as ActionsUsersRequestsItemView
        }

        view.bind(getItem(position))

        return view
    }
}

@SuppressLint("Registered")
@EActivity(R.layout.activity_actions_users_requests_list)
open class ActionsUsersRequestsListActivity : BaseActivity() {
    @Bean
    lateinit var usersRequestsService: UsersRequestsService

    @AfterViews
    fun init() {
        actionsUsersRequestsListMenuLayoutView.setCurrentMenuItem(AppMenuView.Item.ACTIONS)

        val adapter = ActionsUsersRequestsListAdapter(this)

        adapter.setItems(usersRequestsService.getUsersRequests())

        actionsUsersRequestsListView.adapter = adapter

        actionsUsersRequestsListView.setOnItemClickListener { _, _, position, _ ->
            redirect(this)
                    .to(UserRequestActivity_::class.java)
                    .withAnim(R.anim.slide_open_enter, R.anim.slide_open_exit)
                    .withExtra(UserRequestActivity.EXTRA_USER_REQUEST_ID, adapter.getItem(position).id)
                    .go()
        }
    }
}
