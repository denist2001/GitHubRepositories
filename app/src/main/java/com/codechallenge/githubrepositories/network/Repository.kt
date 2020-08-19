package com.codechallenge.githubrepositories.network

import com.codechallenge.githubrepositories.data.model.auth.AuthResponse
import retrofit2.Callback

interface Repository {
    fun authoriseWithoutPass(user: String, callback: Callback<List<AuthResponse>>)

    fun authoriseWithPass(authHeader: String, callback: Callback<List<AuthResponse>>)

    fun authoriseByTwoFA(authHeader: String, twoFA: String, callback: Callback<List<AuthResponse>>)
}