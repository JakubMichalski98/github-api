package com.jakub.github_api.client

import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient

@Component
class GitHubClientImpl(
    private val webClient: WebClient
) :GitHubClient {


    override suspend fun <T> get(endpoint: String, responseType: ParameterizedTypeReference<T>): T {
        return webClient.method(HttpMethod.GET)
            .uri(endpoint)
            .header(HttpHeaders.ACCEPT, "application/json")
            .retrieve()
            .bodyToMono(responseType)
            .awaitSingle()
    }
}