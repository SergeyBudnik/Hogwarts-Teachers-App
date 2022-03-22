package com.bdev.hengschoolteacher.ui.page_fragments.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragment
import com.bdev.hengschoolteacher.ui.views.app.AppLayoutView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_login.*

@AndroidEntryPoint
class LoginPageFragment : BasePageFragment<LoginPageFragmentViewModel>() {
    override fun provideViewModel(): LoginPageFragmentViewModel =
        ViewModelProvider(this).get(LoginPageFragmentViewModelImpl::class.java)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.activity_login, container, false)

    override fun doOnViewCreated() {
        super.doOnViewCreated()

        loginDoLoginView.setOnClickListener {
            fragmentViewModel.login(
                username = loginUsernameView.getText(),
                password = loginPasswordView.getText()
            )
        }
    }

    override fun getAppLayoutView(): AppLayoutView? {
        return null
    }
}
