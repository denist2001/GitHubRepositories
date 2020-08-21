package com.codechallenge.githubrepositories.ui.search

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.codechallenge.githubrepositories.network.Repository
import com.codechallenge.githubrepositories.ui.model.RepoPresentation
import kotlinx.coroutines.flow.Flow

class SearchViewModel @ViewModelInject constructor(
    val repository: Repository
) : ViewModel() {

    lateinit var lastQuery: String

    fun foundedRepositories(authHeader: String, query: String): Flow<PagingData<RepoPresentation>> {
        lastQuery = query
        return repository.searchByRepositories(authHeader, query, 1)
            .cachedIn(viewModelScope)
    }

}