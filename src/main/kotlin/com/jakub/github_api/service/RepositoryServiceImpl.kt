package com.jakub.github_api.service

import com.jakub.github_api.client.RepositoryClient
import com.jakub.github_api.client.RepositoryClientImpl
import com.jakub.github_api.exception.GitHubNotFoundException
import com.jakub.github_api.exception.GitHubServerException
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

       val repositories = repositoryClient.getRepositoriesByUsername(username)

        if (repositories.isEmpty()) {
            return emptyList()
        }

        return repositories.filterNot { it.fork }.map { repo ->
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
    }
}