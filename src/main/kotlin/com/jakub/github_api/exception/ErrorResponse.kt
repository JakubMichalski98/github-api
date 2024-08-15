package com.jakub.github_api.exception

data class ErrorResponse(
    val status: Int,
    val message: String
)