package com.jakub.github_api.client

import com.jakub.github_api.exception.GitHubNotFoundException
import com.jakub.github_api.exception.GitHubServerException
import com.jakub.github_api.model.external.GitHubBranch
import com.jakub.github_api.model.external.GitHubRepository
import org.springframework.stereotype.Component
import org.slf4j.LoggerFactory
import org.springframework.core.ParameterizedTypeReference
import org.springframework.web.reactive.function.client.WebClientResponseException

@Component
class RepositoryClientImpl(
    private val gitHubClient: GitHubClient,
) : RepositoryClient {

    private val logger = LoggerFactory.getLogger(RepositoryClientImpl::class.java)

    override suspend fun getRepositoriesByUsername(username: String): List<GitHubRepository> {
        val endpoint = "/users/$username/repos"

        return try {
            gitHubClient.get(endpoint, object : ParameterizedTypeReference<List<GitHubRepository>>() {})
        } catch (e: GitHubNotFoundException) {
            logger.error("User not found: $username", e)
            throw e
        } catch (e: GitHubServerException) {
            logger.error("Error fetching data from GitHub API at endpoint: $endpoint", e)
            throw e
        } catch (e: Exception) {
            logger.error("Unexpected error: ${e.message}")
            throw e
        }
    }

    override suspend fun getRepositoryBranches(repoName: String, owner: String): List<GitHubBranch> {
        val endpoint = "/repos/$owner/$repoName/branches"

        return try {
            gitHubClient.get(endpoint, object : ParameterizedTypeReference<List<GitHubBranch>>() {})
        } catch (e: WebClientResponseException.NotFound) {
            logger.error("Repository or branches not found: $owner/$repoName")
            throw e
        } catch (e: WebClientResponseException) {
            logger.error("Error fetching branches from GitHub API at endpoint: $endpoint", e)
            throw e
        } catch (e: Exception) {
            logger.error("Unexpected error: ${e.message}")
            throw e
        }
    }
}