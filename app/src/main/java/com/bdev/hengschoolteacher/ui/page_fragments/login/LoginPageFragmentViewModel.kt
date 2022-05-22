package com.bdev.hengschoolteacher.ui.page_fragments.login

import com.bdev.hengschoolteacher.NavGraphDirections
import com.bdev.hengschoolteacher.data.auth.AuthCredentials
import com.bdev.hengschoolteacher.interactors.auth.AuthActionsInteractor
import com.bdev.hengschoolteacher.ui.navigation.NavCommand
import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragmentViewModel
import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragmentViewModelImpl
import com.bdev.hengschoolteacher.ui.page_fragments.loading.LoadingPageFragment
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

interface LoginPageFragmentViewModel : BasePageFragmentViewModel {
    fun login(username: String, password: String)
}

@HiltViewModel
class LoginPageFragmentViewModelImpl @Inject constructor(
    private val authActionsInteractor: AuthActionsInteractor
): LoginPageFragmentViewModel, BasePageFragmentViewModelImpl() {
    override fun goBack() {
        navigate(navCommand = NavCommand.quit())
    }

    override fun login(username: String, password: String) {
        authActionsInteractor.login(
            authCredentials = AuthCredentials(
                username = username,
                password = password
            )
        )
            .onSuccess { onLoginSuccess() }
            .onAuthFail { onLoginFailure() }
            .onOtherFail { onLoginFailure() }
    }

    private fun onLoginSuccess() {
        navigate(
            navCommand = NavCommand.top(
                navDir = NavGraphDirections.loginToLoading()
            )
        )
    }

    private fun onLoginFailure() {
        // todo
    }
}