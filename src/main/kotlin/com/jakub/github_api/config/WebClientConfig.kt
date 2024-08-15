package com.jakub.github_api.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class WebClientConfig(
    private val gitHubProperties: GitHubProperties
) {

    @Bean
    fun webClient(): WebClient {
        return WebClient.builder()
            .baseUrl(gitHubProperties.apiUrl)
            .build()
    }
}