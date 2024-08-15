package com.jakub.github_api.client

interface GitHubClient {
    suspend fun <T> get(endpoint: String, responseType: Class<T>) : T
}