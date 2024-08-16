package com.jakub.github_api.exception

class GitHubServerException(message: String, cause: Throwable? = null) : RuntimeException(message, cause)