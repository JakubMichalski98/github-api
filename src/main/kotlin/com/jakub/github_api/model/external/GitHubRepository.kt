package com.jakub.github_api.model.external

data class GitHubRepository(
    val name: String,
    val owner: GitHubOwner,
    val fork: Boolean,
    val branches_url: String
)