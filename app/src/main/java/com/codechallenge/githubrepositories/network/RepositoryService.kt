package com.codechallenge.githubrepositories.network

import com.codechallenge.githubrepositories.data.model.auth.AuthResponse
import com.codechallenge.githubrepositories.data.model.search.SearchResponse
import retrofit2.Call
import retrofit2.http.*

interface RepositoryService {

    @GET("users/{user}/repos/")
    fun authoriseUser(
        @Path("user") user: String
    ): Call<List<AuthResponse>>

    @Headers(
        "Accept: application/vnd.github.v3+json"
    )
    @GET("authorizations")
    fun authoriseUserByPass(
        @Header("Authorization") authHeader: String
    ): Call<List<AuthResponse>>

    @Headers(
        "x-github-otp:{twoFA}",
        "Accept: application/vnd.github.v3+json"
    )
    @GET("user/repos/")
    fun authoriseUserByTwoFA(
        @Header("Authorization") authHeader: String,
        @Header("twoFA") twoFA: String,
        @Query("note") note: String
    ): Call<List<AuthResponse>>

    @Headers(
        "Accept: application/vnd.github.v3+json"
    )
    @GET("search/repositories/")
    fun searchRepos(
        @Query("q") searchQuery: String,
        @Query("sort") sortType: String,
        @Query("order") order: String,
        @Query("per_page") perPage: Int,
        @Query("page") page: Int
    ): Call<SearchResponse>
}