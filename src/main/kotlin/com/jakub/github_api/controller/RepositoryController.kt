package com.jakub.github_api.controller

import com.jakub.github_api.exception.ErrorResponse
import com.jakub.github_api.exception.GitHubNotFoundException
import com.jakub.github_api.service.RepositoryService
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController

@RestController
class RepositoryController(
    private val repositoryService: RepositoryService
) {
    @GetMapping("/{username}")
    suspend fun getNonForkRepositoriesByUsername(
        @PathVariable username: String,
        @RequestHeader(HttpHeaders.ACCEPT) acceptHeader: String
    ): ResponseEntity<*> {

        if (acceptHeader != "application/json") {
            val errorResponse = ErrorResponse(
                status = HttpStatus.BAD_REQUEST.value(),
                message = "Invalid or missing Accept header, Expected: 'application/json'."
            )
            return ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST)
        }

        return try {
            val repositories = repositoryService.getNonForkRepositoriesByUsername(username)
            ResponseEntity.ok(repositories)
        } catch (e: GitHubNotFoundException) {
            val errorResponse = ErrorResponse(
                status = HttpStatus.NOT_FOUND.value(),
                message = "User with username $username not found"
            )
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse)
        } catch (e: Exception) {
            val errorResponse = ErrorResponse(
                status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
                message = "An unexpected error occurred"
            )
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse)
        }
    }
}

