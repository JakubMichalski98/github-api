package com.jakub.github_api.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "github")
class GitHubProperties {
    lateinit var apiUrl: String
}