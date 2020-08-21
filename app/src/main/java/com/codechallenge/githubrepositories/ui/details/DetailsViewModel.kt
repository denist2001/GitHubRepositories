package com.codechallenge.githubrepositories.ui.details

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.codechallenge.githubrepositories.network.Repository
import kotlinx.coroutines.flow.Flow

class DetailsViewModel @ViewModelInject constructor(
    val repository: Repository
) : ViewModel() {

    fun requestWatchers(
        userName: String,
        repositoryName: String,
        totalCount: Int
    ): Flow<PagingData<String>> {
        return repository.requestWatchers(userName, repositoryName, totalCount, 1)
            .cachedIn(viewModelScope)
    }

}