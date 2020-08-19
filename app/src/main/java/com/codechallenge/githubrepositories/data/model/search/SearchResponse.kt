package com.codechallenge.githubrepositories.data.model.search

data class SearchResponse(
    val total_count: Int,
    val incomplete_results: Boolean,
    val items: List<Item>
)