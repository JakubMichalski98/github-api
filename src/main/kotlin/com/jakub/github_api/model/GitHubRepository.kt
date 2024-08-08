package com.jakub.github_api.model

data class GitHubRepository(
    val repositoryName: String,
    val ownerLogin: String,
    val branches: List<GitHubBranch>
)