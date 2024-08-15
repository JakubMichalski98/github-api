package com.jakub.github_api.client

import com.jakub.github_api.model.external.GitHubBranch
import com.jakub.github_api.model.external.GitHubRepository
import org.springframework.stereotype.Component
import org.slf4j.LoggerFactory
import org.springframework.core.ParameterizedTypeReference
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestClientException

@Component
class RepositoryClientImpl(
    private val gitHubClient: GitHubClient,
) : RepositoryClient {

    private val logger = LoggerFactory.getLogger(RepositoryClientImpl::class.java)

    override suspend fun getRepositoriesByUsername(username: String): List<GitHubRepository> {
        val endpoint = "/users/$username/repos"

        return try {
            gitHubClient.get(endpoint, object : ParameterizedTypeReference<List<GitHubRepository>>() {})
        } catch (e: HttpClientErrorException.NotFound) {
            logger.error("User not found: $username")
            throw e
        } catch (e: RestClientException) {
            logger.error("Error fetching data from GitHub API: ${e.message}")
            throw e
        } catch (e: Exception) {
            logger.error("Unexpected error: ${e.message}")
            throw e
        }
    }

    override suspend fun getRepositoryBranches(repoName: String, owner: String): List<GitHubBranch> {
        val endpoint = "/repos/$owner/$repoName/branches"

        return try {
            return gitHubClient.get(endpoint, object : ParameterizedTypeReference<List<GitHubBranch>>() {})
        } catch (e: HttpClientErrorException.NotFound) {
            logger.error("Repository or branches not found: $owner/$repoName")
            emptyList()
        } catch (e: RestClientException) {
            logger.error("Error fetching branches from GitHub API: ${e.message}")
            emptyList()
        } catch (e: Exception) {
            logger.error("Unexpected error: ${e.message}")
            emptyList()
        }
    }
}