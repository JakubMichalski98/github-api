package com.jakub.github_api.service

import io.mockk.*
import com.jakub.github_api.client.RepositoryClient
import com.jakub.github_api.model.external.GitHubBranch
import com.jakub.github_api.model.external.GitHubCommit
import com.jakub.github_api.model.external.GitHubOwner
import com.jakub.github_api.model.external.GitHubRepository
import com.jakub.github_api.model.response.BranchResponse
import com.jakub.github_api.model.response.UserRepositoryResponse
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class RepositoryServiceTests {
    private lateinit var repositoryClient: RepositoryClient
    private lateinit var repositoryService: RepositoryServiceImpl

    @BeforeEach
    fun setUp() {
        repositoryClient = mockk()
        repositoryService = RepositoryServiceImpl(repositoryClient)
    }

    @Test
    fun `test getNonForkRepositoriesByUsername returns filtered repositories`() = runBlocking {
        val username = "testuser"
        val repositories = listOf(
            GitHubRepository("repo1", GitHubOwner("owner1"), false, "/repos/owner1/branches"),
            GitHubRepository("repo2", GitHubOwner("owner2"), true, "/repos/owner2/repo2/branches")
        )

        val branches = listOf(
            GitHubBranch("branch1", GitHubCommit("sha1")),
            GitHubBranch("branch2", GitHubCommit("sha2"))
        )

        coEvery { repositoryClient.getRepositoriesByUsername(username) } returns repositories
        coEvery { repositoryClient.getRepositoryBranches("repo1", "owner1") } returns branches

        val expectedResponse = listOf(
            UserRepositoryResponse(
                repositoryName = "repo1",
                ownerLogin = "owner1",
                branches = branches.map { branch ->
                    BranchResponse(
                        branchName = branch.name,
                        lastCommitSha = branch.commit.sha
                    )
                }
            )
        )

        val result = repositoryService.getNonForkRepositoriesByUsername(username)
        assertEquals(expectedResponse, result)

        coVerify { repositoryClient.getRepositoriesByUsername(username)
        repositoryClient.getRepositoryBranches("repo1", "owner1")
        }
    }

    @Test
    fun `test getNonForkRepositoriesByUsername returns empty list when no repositories found`() = runBlocking {
        val username = "testuser"

        coEvery { repositoryClient.getRepositoriesByUsername(username) } returns emptyList()

        val result = repositoryService.getNonForkRepositoriesByUsername(username)
        assertTrue(result.isEmpty())

        coVerify { repositoryClient.getRepositoriesByUsername(username) }
    }
}