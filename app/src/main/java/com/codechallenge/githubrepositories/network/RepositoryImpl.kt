package com.codechallenge.githubrepositories.network

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.codechallenge.githubrepositories.data.model.auth.AuthResponse
import com.codechallenge.githubrepositories.ui.model.RepoPresentation
import com.codechallenge.githubrepositories.util.ResponsesMapper
import kotlinx.coroutines.flow.Flow
import retrofit2.Callback
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RepositoryImpl @Inject constructor() : Repository {

    companion object {
        private const val PROJECT_NAME = "GitHubRepositories"
    }

    @Inject
    lateinit var networkService: RepositoryService

    @Inject
    lateinit var mapper: ResponsesMapper

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

    override fun searchByRepositories(
        authHeader: String,
        query: String,
        initialLoadSize: Int
    ): Flow<PagingData<RepoPresentation>> {
        val pagingSource = GetReposFlowPagingSource(query, networkService, mapper, authHeader)
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = true,
                prefetchDistance = 5,
                initialLoadSize = initialLoadSize
            ),
            pagingSourceFactory = { pagingSource }
        ).flow
    }

    override fun requestWatchers(
        userName: String,
        repositoryName: String,
        total_count: Int,
        initialLoadSize: Int
    ): Flow<PagingData<String>> {
        val pagingSource = GetWatchersFlowPagingSource(
            networkService,
            mapper,
            userName,
            repositoryName,
            total_count
        )
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = true,
                prefetchDistance = 5,
                initialLoadSize = initialLoadSize
            ),
            pagingSourceFactory = { pagingSource }
        ).flow
    }
}