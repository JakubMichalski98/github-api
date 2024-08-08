package com.jakub.github_api.service

import com.jakub.github_api.client.RepositoryClient
import com.jakub.github_api.client.RepositoryClientImpl
import com.jakub.github_api.model.GitHubRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class RepositoryServiceImpl(
    private val repositoryClient: RepositoryClient
) : RepositoryService {

    private val logger = LoggerFactory.getLogger(RepositoryClientImpl::class.java)

    override fun getNonForkRepositoriesByUsername(username: String): List<GitHubRepository> {
        return try {
            repositoryClient.getNonForkRepositoriesByUsername(username)
        } catch (e: Exception) {
            logger.error("Failed to fetch repositories for user: $username", e)
            emptyList()
        }
    }
}