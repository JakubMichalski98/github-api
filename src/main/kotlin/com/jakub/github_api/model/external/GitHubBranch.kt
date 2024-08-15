package com.jakub.github_api.model.external

data class GitHubBranch(
    val name: String,
    val commit: GitHubCommit
)