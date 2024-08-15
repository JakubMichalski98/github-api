package com.jakub.github_api.model.response

data class UserRepositoryResponse(
    val repositoryName: String,
    val ownerLogin: String,
    val branches: List<BranchResponse>
)