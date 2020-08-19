package com.codechallenge.githubrepositories.ui.search

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.codechallenge.githubrepositories.network.Repository

class SearchViewModel @ViewModelInject constructor(
    val repository: Repository
) : ViewModel(){

}

sealed class LoginViewModelAction {
    class AuthorisationRequest(val user: String, val password: String, val token: String?) :
        LoginViewModelAction()
}

sealed class LoginViewModelState {
    class UserAuthorised(val userName: String, val authHeader: String) : LoginViewModelState()
    class AuthorizationError(val message: String) : LoginViewModelState()
}