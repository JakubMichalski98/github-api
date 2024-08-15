package com.jakub.github_api.service

import com.jakub.github_api.client.RepositoryClient
import com.jakub.github_api.client.RepositoryClientImpl
import com.jakub.github_api.model.response.BranchResponse
import com.jakub.github_api.model.response.UserRepositoryResponse
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClientException
import org.springframework.web.reactive.function.client.WebClientResponseException

@Service
class RepositoryServiceImpl(
    private val repositoryClient: RepositoryClient
) : RepositoryService {

    private val logger = LoggerFactory.getLogger(RepositoryClientImpl::class.java)

    override suspend fun getNonForkRepositoriesByUsername(username: String): List<UserRepositoryResponse> {
        return try {
           val repositories = repositoryClient.getRepositoriesByUsername(username)

            if (repositories.isEmpty()) {
                return emptyList()
            }

            repositories.filterNot { it.fork }.map { repo ->
                val branches = repositoryClient.getRepositoryBranches( repo.name, repo.owner.login)
                UserRepositoryResponse(
                    repositoryName = repo.name,
                    ownerLogin = repo.owner.login,
                    branches = branches.map { branch ->
                        BranchResponse(
                            branchName = branch.name,
                            lastCommitSha = branch.commit.sha
                        )
                    }
                )
            }
        } catch (e: WebClientResponseException.NotFound) {
            logger.error("User not found: $username")
            throw e
        } catch (e: WebClientResponseException) {
            logger.error("Error fetching data from GitHub API: ${e.message}", e)
            throw e
        } catch (e: Exception) {
            logger.error("Unexpected error occurred: ${e.message}", e)
            throw RuntimeException("An error occurred while fetching repositories")
        }
    }
}