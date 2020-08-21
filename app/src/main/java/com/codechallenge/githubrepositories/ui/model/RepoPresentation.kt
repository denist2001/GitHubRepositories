package com.codechallenge.githubrepositories.ui.model

data class RepoPresentation(
    val id: Int,
    val avatarImage: String,
    val repositoryName: String,
    val description: String,
    val numberOfForks: Int,
    val ownerName: String,
    val watchersCount: Int
)
