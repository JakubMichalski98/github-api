package com.jakub.github_api.client

import com.fasterxml.jackson.databind.ObjectMapper
import com.jakub.github_api.model.GitHubBranch
import com.jakub.github_api.model.GitHubRepository
import org.springframework.stereotype.Component
import org.slf4j.LoggerFactory
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestClientException

@Component
class RepositoryClientImpl(
    private val gitHubClient: GitHubClient,
    private val objectMapper: ObjectMapper
) : RepositoryClient {

    private val logger = LoggerFactory.getLogger(RepositoryClientImpl::class.java)

    override fun getNonForkRepositoriesByUsername(username: String): List<GitHubRepository> {
        val endpoint = "/users/$username/repos"

        return try {
            val responseBody = gitHubClient.get(endpoint, String::class.java)
            val jsonArray = objectMapper.readTree(responseBody)

            jsonArray
                .filter { !it.get("fork").asBoolean() }
                .mapNotNull { jsonNode ->
                    val repoName = jsonNode.get("name").asText()
                    val ownerLogin = jsonNode.get("owner")?.get("login")?.asText()
                    if (repoName != null && ownerLogin != null) {
                        val branches = getRepositoryBranches(repoName, ownerLogin)
                        GitHubRepository(repoName, ownerLogin, branches)
                    }
                    else {
                        null
                    }
                }
        } catch (e: HttpClientErrorException.NotFound) {
            logger.error("User not found: $username")
            emptyList()
        } catch (e: RestClientException) {
            logger.error("Error fetching data from GitHub API: ${e.message}")
            emptyList()
        } catch (e: Exception) {
            logger.error("Unexpected error: ${e.message}")
            emptyList()
        }
    }

    override fun getRepositoryBranches(repoName: String, owner: String): List<GitHubBranch> {
        val endpoint = "/repos/$owner/$repoName/branches"

        return try {
            val responseBody = gitHubClient.get(endpoint, String::class.java)
            val jsonArray = objectMapper.readTree(responseBody)

            jsonArray
                .mapNotNull { jsonNode ->
                    val branchName = jsonNode.get("name")?.asText()
                    val commitSha = jsonNode.get("commit")?.get("sha")?.asText()
                    if (branchName != null && commitSha != null) {
                        GitHubBranch(branchName, commitSha)
                    }
                    else {
                        null
                    }
                }
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