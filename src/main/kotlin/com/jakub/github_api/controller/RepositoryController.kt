package com.jakub.github_api.controller

import com.jakub.github_api.client.RepositoryClientImpl
import com.jakub.github_api.model.external.GitHubRepository
import com.jakub.github_api.model.response.UserRepositoryResponse
import com.jakub.github_api.service.RepositoryService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestClientException

@RestController
class RepositoryController(
    private val repositoryService: RepositoryService
) {

    private val logger = LoggerFactory.getLogger(RepositoryClientImpl::class.java)

    @GetMapping("/{username}")
    suspend fun getNonForkRepositoriesByUsername(
        @PathVariable username: String,
        @RequestHeader(HttpHeaders.ACCEPT) acceptHeader: String
    ): ResponseEntity<Any> {
        if (acceptHeader != "application/json") {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(
                mapOf("status" to HttpStatus.NOT_ACCEPTABLE.value(), "message" to "Accept header must be application/json")
            )
        }

        return try {
            val repositories: List<UserRepositoryResponse> = repositoryService.getNonForkRepositoriesByUsername(username)

            if (repositories.isEmpty()) {
                ResponseEntity.status(HttpStatus.OK).body(emptyList<GitHubRepository>())
            } else {
                ResponseEntity.ok(repositories)
            }
        } catch (e: HttpClientErrorException.NotFound) {
            logger.error("User $username not found", e)
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                mapOf("status" to HttpStatus.NOT_FOUND.value(), "message" to "User not found")
            )
        } catch (e: RestClientException) {
            logger.error("Error fetching data from GitHub API: ${e.message}", e)
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                mapOf("status" to HttpStatus.INTERNAL_SERVER_ERROR.value(), "message" to "Error fetching data from GitHub API")
            )
        } catch (e: Exception) {
            logger.error("An unexpected error occurred")
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                mapOf("status" to HttpStatus.INTERNAL_SERVER_ERROR.value(), "message" to "An unexpected error occurred")
            )
        }
    }
}