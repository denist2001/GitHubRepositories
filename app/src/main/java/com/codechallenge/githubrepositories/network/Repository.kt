package com.codechallenge.githubrepositories.network

import androidx.paging.PagingData
import com.codechallenge.githubrepositories.data.model.auth.AuthResponse
import com.codechallenge.githubrepositories.ui.model.RepoPresentation
import kotlinx.coroutines.flow.Flow
import retrofit2.Callback

interface Repository {
    fun authoriseWithoutPass(user: String, callback: Callback<List<AuthResponse>>)

    fun authoriseWithPass(authHeader: String, callback: Callback<List<AuthResponse>>)

    fun authoriseByTwoFA(authHeader: String, twoFA: String, callback: Callback<List<AuthResponse>>)

    fun searchByRepositories(
        authHeader: String,
        query: String,
        initialLoadSize: Int
    ): Flow<PagingData<RepoPresentation>>

    fun requestWatchers(
        userName: String,
        repositoryName: String,
        total_count: Int,
        initialLoadSize: Int
    ): Flow<PagingData<String>>
}