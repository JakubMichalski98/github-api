package com.jakub.github_api.client

import com.jakub.github_api.exception.GitHubNotFoundException
import com.jakub.github_api.exception.GitHubServerException
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Component
class GitHubClientImpl(
    private val webClient: WebClient
) :GitHubClient {


    override suspend fun <T> get(endpoint: String, responseType: ParameterizedTypeReference<T>): T {
        return webClient.method(HttpMethod.GET)
            .uri(endpoint)
            .header(HttpHeaders.ACCEPT, "application/json")
            .retrieve()
            .onStatus({ status -> status.is4xxClientError || status.is5xxServerError }) { response ->
                response.bodyToMono(String::class.java)
                    .flatMap { body ->
                        val exception = when (response.statusCode()) {
                            HttpStatus.NOT_FOUND -> GitHubNotFoundException("Resource not found at endpoint $endpoint: $body")
                            else -> GitHubServerException("Error fetching from GitHub API: $body")
                        }
                        Mono.error(exception)
                    }
            }
            .bodyToMono(responseType)
            .awaitSingle()
    }
}