package com.jakub.github_api.service

import com.jakub.github_api.model.external.GitHubRepository
import com.jakub.github_api.model.response.UserRepositoryResponse

interface RepositoryService {
    suspend fun getNonForkRepositoriesByUsername(username: String) : List<UserRepositoryResponse>
}