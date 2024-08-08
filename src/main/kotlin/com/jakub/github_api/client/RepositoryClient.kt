package com.jakub.github_api.client

import com.jakub.github_api.model.GitHubBranch
import com.jakub.github_api.model.GitHubRepository

interface RepositoryClient {
    fun getNonForkRepositoriesByUsername(username: String) : List<GitHubRepository>
    fun getRepositoryBranches(repoName: String, owner: String,) : List<GitHubBranch>
}