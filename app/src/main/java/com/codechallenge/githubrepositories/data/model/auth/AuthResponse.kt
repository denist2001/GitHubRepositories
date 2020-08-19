package com.codechallenge.githubrepositories.data.model.auth

data class AuthResponse(
    val id: String,
    val url: String,
    val app: App,
    val token: String,
    val hashed_token: String,
    val token_last_eight: String,
    val note: String,
    val note_url: String,
    val created_at: String,
    val updated_at: String,
    val scopes: ArrayList<String>,
    val fingerprint: String
)