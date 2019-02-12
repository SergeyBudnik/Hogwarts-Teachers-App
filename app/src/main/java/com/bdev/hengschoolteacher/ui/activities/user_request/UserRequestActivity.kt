package com.bdev.hengschoolteacher.ui.activities.user_request

import android.annotation.SuppressLint
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.service.UsersRequestsService
import com.bdev.hengschoolteacher.ui.activities.BaseActivity
import kotlinx.android.synthetic.main.activity_user_request.*
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EActivity
import org.androidannotations.annotations.Extra
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("Registered")
@EActivity(R.layout.activity_user_request)
open class UserRequestActivity : BaseActivity() {
    companion object {
        const val EXTRA_USER_REQUEST_ID = "EXTRA_USER_REQUEST_ID"
    }

    @Extra(EXTRA_USER_REQUEST_ID)
    @JvmField
    var userRequestId: Long = 0

    @Bean
    lateinit var usersRequestsService: UsersRequestsService

    @AfterViews
    fun init() {
        userRequestHeaderView.setLeftButtonAction { doFinish() }

        val userRequest = usersRequestsService.getUserRequest(userRequestId) ?: throw RuntimeException()

        userRequestNameView.text = userRequest.name
        userRequestDateView.text = SimpleDateFormat("dd.MM.yyyy", Locale.US).format(Date(userRequest.date))
        userRequestPhoneView.bind(userRequest.phone)
    }

    override fun onBackPressed() {
        doFinish()
    }

    private fun doFinish() {
        finish()
        overridePendingTransition(R.anim.slide_close_enter, R.anim.slide_close_exit)
    }
}
