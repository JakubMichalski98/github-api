package com.jakub.github_api.client

import com.jakub.github_api.exception.GitHubNotFoundException
import com.jakub.github_api.exception.GitHubServerException
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.web.reactive.function.client.WebClient

data class CustomType(val field: String)

class GitHubClientTests {
    private lateinit var mockWebServer: MockWebServer
    private lateinit var webClient: WebClient
    private lateinit var gitHubClient: GitHubClientImpl

    @BeforeEach
    fun setUp() {
        mockWebServer = MockWebServer()

        mockWebServer.start()

        webClient = WebClient.builder()
            .baseUrl(mockWebServer.url("/").toString())
            .build()

        gitHubClient = GitHubClientImpl(webClient)
    }

    @AfterEach
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `test successful response with String type`() = runBlocking {
        val responseBody = "Mocked Response"
        val endpoint = "/test-endpoint"
        val typeReference = object : ParameterizedTypeReference<String>() {}

        mockWebServer.enqueue(MockResponse()
            .setResponseCode(200)
            .setBody(responseBody)
            .addHeader(HttpHeaders.CONTENT_TYPE, "application/json")
        )

        val result = gitHubClient.get(endpoint, typeReference)

        assertEquals(responseBody, result)

        val request = mockWebServer.takeRequest()
        assertEquals(endpoint, request.path)
        assertEquals(HttpMethod.GET.name(), request.method)
    }

    @Test
    fun `test successful response with CustomType`() = runBlocking {

        val responseBody = CustomType("Custom Value")
        val endpoint = "/custom-endpoint"
        val typeReference = object : ParameterizedTypeReference<CustomType>() {}

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody("{\"field\": \"Custom Value\"}")
                .addHeader(HttpHeaders.CONTENT_TYPE, "application/json")
        )

        val result = gitHubClient.get(endpoint, typeReference)
        assertEquals(responseBody, result)

        val request = mockWebServer.takeRequest()
        assertEquals(endpoint, request.path)
        assertEquals(HttpMethod.GET.name(), request.method)
    }

    @Test
    fun `test 404 Not Found throws GitHubNotFoundException`() = runBlocking {
        val endpoint = "/not-found-endpoint"
        val typeReference = object : ParameterizedTypeReference<String>() {}

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(404)
                .setBody("Not Found")
                .addHeader(HttpHeaders.CONTENT_TYPE, "application/json")
        )

        assertThrows<GitHubNotFoundException> {
            gitHubClient.get(endpoint, typeReference)
        }

        val request = mockWebServer.takeRequest()
        assertEquals(endpoint, request.path)
        assertEquals(HttpMethod.GET.name(), request.method)
    }

    @Test
    fun `test 500 Internal Server Error`() = runBlocking {
        val endpoint = "/server-error-endpoint"
        val typeReference = object : ParameterizedTypeReference<String>() {}

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(500)
                .setBody("Internal Server Error")
                .addHeader(HttpHeaders.CONTENT_TYPE, "application/json")
        )

        assertThrows<GitHubServerException> {
            gitHubClient.get(endpoint, typeReference)
        }

        val request = mockWebServer.takeRequest()
        assertEquals(endpoint, request.path)
        assertEquals(HttpMethod.GET.name(), request.method)
    }
}