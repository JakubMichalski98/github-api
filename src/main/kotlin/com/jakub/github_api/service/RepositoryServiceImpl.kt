package com.jakub.github_api.service

import com.jakub.github_api.client.RepositoryClient
import com.jakub.github_api.client.RepositoryClientImpl
import com.jakub.github_api.model.GitHubRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestClientException

@Service
class RepositoryServiceImpl(
    private val repositoryClient: RepositoryClient
) : RepositoryService {

    private val logger = LoggerFactory.getLogger(RepositoryClientImpl::class.java)

    override suspend fun getNonForkRepositoriesByUsername(username: String): List<GitHubRepository> {
        return try {
            repositoryClient.getNonForkRepositoriesByUsername(username)
        } catch (e: HttpClientErrorException.NotFound) {
            logger.error("User not found: $username")
            throw e
        } catch (e: RestClientException) {
            logger.error("Error fetching data from GitHub API: ${e.message}", e)
            throw e
        } catch (e: Exception) {
            logger.error("Unexpected error occurred: ${e.message}", e)
            throw RuntimeException("An error occurred while fetching repositories")
        }
    }
}