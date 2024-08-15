package com.jakub.github_api.service

import com.jakub.github_api.model.GitHubRepository

interface RepositoryService {
    suspend fun getNonForkRepositoriesByUsername(username: String) : List<GitHubRepository>
}