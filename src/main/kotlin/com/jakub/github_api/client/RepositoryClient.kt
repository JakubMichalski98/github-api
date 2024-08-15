package com.jakub.github_api.client

import com.jakub.github_api.model.external.GitHubBranch
import com.jakub.github_api.model.external.GitHubRepository

interface RepositoryClient {
    suspend fun getRepositoriesByUsername(username: String) : List<GitHubRepository>
    suspend fun getRepositoryBranches(repoName: String, owner: String,) : List<GitHubBranch>
}