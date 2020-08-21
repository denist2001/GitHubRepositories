package com.codechallenge.githubrepositories.network

import androidx.paging.PagingSource
import com.codechallenge.githubrepositories.util.ResponsesMapper
import javax.inject.Inject

class GetWatchersFlowPagingSource @Inject constructor(
    private val networkService: RepositoryService,
    private val mapper: ResponsesMapper,
    private val userName: String,
    private val repositoryName: String,
    private val total_count: Int
) : PagingSource<Int, String>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, String> {
        val position = params.key ?: 1

        return try {
            networkService.requestWatchers(
                userName = userName,
                repositoryName = repositoryName,
                perPage = 20,
                page = position
            ).run {
                val presentations = mapper.transform(this)

                LoadResult.Page(
                    data = presentations,
                    prevKey = if (position == 1) null else position - 1,
                    nextKey = if (position == total_count / 20) null else position + 1
                )
            }
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

}
