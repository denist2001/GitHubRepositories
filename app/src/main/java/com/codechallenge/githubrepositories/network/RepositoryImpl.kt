package com.codechallenge.githubrepositories.network

import com.codechallenge.githubrepositories.data.model.auth.AuthResponse
import retrofit2.Callback
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RepositoryImpl @Inject constructor() : Repository {

    companion object {
        private const val USERS_ENDPOINT = "users"
        private const val USER_ENDPOINT = "user"
        private const val REPOS_ENDPOINT = "repos"
        private const val AUTHORIZATIONS_ENDPOINT = "authorizations"
        private const val PROJECT_NAME = "GitHubRepositories"
    }

    @Inject
    lateinit var networkService: RepositoryService

    private val apiKey = "BuildConfig.API_KEY"

    override fun authoriseWithoutPass(
        user: String,
        callback: Callback<List<AuthResponse>>
    ) {
        val callResponse = networkService.authoriseUser(user)
        callResponse.enqueue(callback)
    }

    override fun authoriseWithPass(
        authHeader: String,
        callback: Callback<List<AuthResponse>>
    ) {
        val callResponse = networkService.authoriseUserByPass(authHeader)
        callResponse.enqueue(callback)
    }

    override fun authoriseByTwoFA(
        authHeader: String,
        twoFA: String,
        callback: Callback<List<AuthResponse>>
    ) {
        val callResponse = networkService.authoriseUserByTwoFA(authHeader, twoFA, PROJECT_NAME)
        callResponse.enqueue(callback)
    }
}