package com.bdev.hengschoolteacher.ui.page_fragments.login

import com.bdev.hengschoolteacher.data.auth.AuthCredentials
import com.bdev.hengschoolteacher.interactors.auth.AuthActionsInteractor
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
        // todo
    }

    override fun login(username: String, password: String) {
        authActionsInteractor.login(AuthCredentials(username, password))
            .onSuccess { onLoginSuccess() }
            .onAuthFail { onLoginFailure() }
            .onOtherFail { onLoginFailure() }
    }

    private fun onLoginSuccess() {
        // LoadingPageFragment.redirectToSibling(this) ToDo: navigate
    }

    private fun onLoginFailure() {
        // todo
    }
}