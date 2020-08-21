package com.codechallenge.githubrepositories.util

import com.codechallenge.githubrepositories.data.model.search.SearchResponse
import com.codechallenge.githubrepositories.data.model.watchers.WatchersResponse
import com.codechallenge.githubrepositories.ui.model.RepoPresentation
import javax.inject.Inject

class ResponsesMapper @Inject constructor() {

    fun transform(response: SearchResponse): List<RepoPresentation> {
        return with(response) {
            items.map {
                RepoPresentation(
                    id = it.id,
                    avatarImage = it.owner.avatar_url,
                    repositoryName = it.name,
                    description = it.description,
                    numberOfForks = it.forks_count,
                    ownerName = it.owner.login,
                    watchersCount = it.watchers_count
                )
            }
        }
    }

    fun transform(responses: List<WatchersResponse>): List<String> {
        return with(responses) {
            map {
                it.login
            }
        }
    }

}