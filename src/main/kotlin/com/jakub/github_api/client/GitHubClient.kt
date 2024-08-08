package com.jakub.github_api.client

interface GitHubClient {
    fun <T> get(endpoint: String, responseType: Class<T>) : T
}