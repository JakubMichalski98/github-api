package com.jakub.github_api.client

import com.jakub.github_api.config.GitHubProperties
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class GitHubClientImpl(
    private val gitHubProperties: GitHubProperties
) :GitHubClient {
    private val restTemplate = RestTemplate()

    public override fun <T> get(endpoint: String, responseType: Class<T>): T {

        val url = "${gitHubProperties.apiUrl}$endpoint"

        val headers = HttpHeaders().apply {
            accept = listOf(org.springframework.http.MediaType.APPLICATION_JSON)
        }
        val request = HttpEntity<String>(headers)

        return restTemplate.exchange(url, HttpMethod.GET, request, responseType).body
            ?: throw RuntimeException("Failed to fetch data from GitHub API")
    }
}