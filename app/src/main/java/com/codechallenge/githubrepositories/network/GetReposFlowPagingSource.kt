package com.codechallenge.githubrepositories.network

import androidx.paging.PagingSource
import com.codechallenge.githubrepositories.ui.model.RepoPresentation
import com.codechallenge.githubrepositories.util.ResponsesMapper
import javax.inject.Inject

class GetReposFlowPagingSource @Inject constructor(
    private val searchQuery: String,
    private val networkService: RepositoryService,
    private val mapper: ResponsesMapper,
    private val authHeader: String
) : PagingSource<Int, RepoPresentation>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RepoPresentation> {
        val position = params.key ?: 1

        return try {
            networkService.searchRepos(
                authHeader = authHeader,
                searchQuery = searchQuery,
                sortType = "stars",
                order = "desc",
                perPage = 20,
                page = position
            ).run {
                val presentations = mapper.transform(this)

                LoadResult.Page(
                    data = presentations,
                    prevKey = if (position == 1) null else position - 1,
                    nextKey = if (position == this.total_count) null else position + 1
                )
            }
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

}
