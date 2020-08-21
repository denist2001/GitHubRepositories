package com.codechallenge.githubrepositories.ui.login

import android.text.TextUtils
import android.util.Base64
import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.codechallenge.githubrepositories.data.model.auth.AuthResponse
import com.codechallenge.githubrepositories.network.Repository
import retrofit2.Call
import retrofit2.Callback

class LoginViewModel @ViewModelInject constructor(
    val repository: Repository
) : ViewModel() {

    val state = MutableLiveData<LoginViewModelState>()
    private var twoFactorRequested = false
    private var twoFactorToken: String = ""
    private var authHeader: String = ""
    private var userName: String = ""

    fun onAction(action: LoginViewModelAction) {
        when (action) {
            is LoginViewModelAction.AuthorisationRequest -> authoriseUser(
                action.user,
                action.password,
                action.token
            )
        }
    }

    private fun authoriseUser(user: String, password: String, token: String?) {
        userName = user
        if (password.isEmpty()) {
            repository.authoriseWithoutPass(user, dataCallback)
        } else if (twoFactorRequested) {
            val base = Base64.encodeToString("$user:$password".toByteArray(), Base64.NO_WRAP)
            authHeader = "Basic $base"
            repository.authoriseWithPass(authHeader, dataCallback)
        } else {
            authHeader = if (TextUtils.isEmpty(twoFactorToken)) {
                val base = Base64.encodeToString(
                    "$user:$password".toByteArray(),
                    Base64.NO_WRAP
                )
                "Basic $base"
            } else {
                "token $twoFactorToken"
            }
            if (token.isNullOrEmpty() || twoFactorToken.isNotEmpty()) {
                repository.authoriseWithPass(authHeader, dataCallback)
            } else {
                repository.authoriseByTwoFA(authHeader, token, dataCallback)
            }
        }
    }

    private val dataCallback = object : Callback<List<AuthResponse>> {
        override fun onFailure(call: Call<List<AuthResponse>>, t: Throwable) {
            t.message?.let { state.postValue(LoginViewModelState.AuthorizationError(it)) }
        }

        override fun onResponse(
            call: Call<List<AuthResponse>>,
            authResponse: retrofit2.Response<List<AuthResponse>>
        ) {
            when (authResponse.code()) {
                401 -> state.postValue(LoginViewModelState.AuthorizationError("Two-factor authentication is active, please enter code."))
                403 -> state.postValue(LoginViewModelState.AuthorizationError("Maximum number of login attempts exceeded. Please try again later."))
                200 -> authResponse.body()
                    ?.let {
                        state.postValue(
                            LoginViewModelState.UserAuthorised(
                                userName,
                                authHeader
                            )
                        )
                    }
                else -> state.postValue(LoginViewModelState.AuthorizationError("Cannot fetch data from GitHub!"))
            }
            Log.d("Login", "onResponse: ")
        }
    }
}

sealed class LoginViewModelAction {
    class AuthorisationRequest(val user: String, val password: String, val token: String?) :
        LoginViewModelAction()
}

sealed class LoginViewModelState {
    class UserAuthorised(val userName: String, val authHeader: String) : LoginViewModelState()
    class AuthorizationError(val message: String) : LoginViewModelState()
}