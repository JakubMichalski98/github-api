package com.jakub.github_api.client

import org.springframework.core.ParameterizedTypeReference

interface GitHubClient {
    suspend fun <T> get(endpoint: String, responseType: ParameterizedTypeReference<T>) : T
}