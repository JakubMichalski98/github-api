package com.jakub.github_api.client

import com.jakub.github_api.model.external.GitHubBranch
import com.jakub.github_api.model.external.GitHubCommit
import io.mockk.*
import com.jakub.github_api.model.external.GitHubOwner
import com.jakub.github_api.model.external.GitHubRepository
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class RepositoryClientTests {

    private lateinit var gitHubClient: GitHubClient
    private lateinit var repositoryClient: RepositoryClientImpl

    @BeforeEach
    fun setUp() {
        gitHubClient = mockk()
        repositoryClient = RepositoryClientImpl(gitHubClient)
    }

    @Test
    fun `test getRepositoriesByUsername returns list of GitHubRepository`() = runBlocking {
        val username = "testuser"
        val endpoint = "/users/$username/repos"
        val repositories = listOf(
            GitHubRepository("repo1", GitHubOwner("owner1"), false, "/repos/owner1/repo1/branches"),
            GitHubRepository("repo2", GitHubOwner("owner2"), true, "/repos/owner2/repo2/branches")
        )

        coEvery { gitHubClient.get<List<GitHubRepository>>(endpoint, any()) } returns repositories

        val result = repositoryClient.getRepositoriesByUsername(username)

        assertEquals(repositories, result)
        coVerify { gitHubClient.get<List<GitHubRepository>>(endpoint, any()) }
    }

    @Test
    fun `test getRepositoryBranches returns list of GitHubBranch`() = runBlocking {
        val repoOwner = "testuser"
        val repoName = "testrepo"
        val endpoint = "/repos/$repoOwner/$repoName/branches"
        val branches = listOf(
            GitHubBranch("branch1", GitHubCommit("sha1")),
            GitHubBranch("branch2", GitHubCommit("sha2"))
        )

        coEvery { gitHubClient.get<List<GitHubBranch>>(endpoint, any()) } returns branches

        val result = repositoryClient.getRepositoryBranches(repoName, repoOwner)

        assertEquals(branches, result)
        coVerify { gitHubClient.get<List<GitHubBranch>>(endpoint, any()) }

    }
}