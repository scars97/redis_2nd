package com.example.infrastructure.controller

import com.example.application.usecase.MovieUseCase
import com.example.infrastructure.config.IntegrationTestSupport
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.http.HttpStatus
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import java.util.concurrent.CompletableFuture

class MovieSearchApiTest: IntegrationTestSupport() {

    @MockitoBean
    private lateinit var movieUseCase: MovieUseCase

    @DisplayName("API 호출 시, 200 Ok 를 응답 받는다.")
    @Test
    fun whenApiCallThenReturn200Ok() {
        // given
        `when`(movieUseCase.getAvailableMovies(any(), any())).thenReturn(emptyList())

        // when
        val result = mockMvc.perform(
            get("/api/movies")
        ).andReturn()

        // then
        assertThat(result.response.status).isEqualTo(HttpStatus.OK.value())
    }

    @DisplayName("1분 안에 50회 초과 요청 시, 429 Too Many Requests 가 응답된다.")
    @Test
    fun when50RequestsInAMinuteThenReturn429TooManyRequests() {
        // given
        `when`(movieUseCase.getAvailableMovies(any(), any())).thenReturn(emptyList())

        val resultList = mutableListOf<MvcResult>()

        // when
        repeat(55) {
            val result = mockMvc.perform(
                get("/api/movies")
                    .header("X-Forwarded-For", "127.0.0.1")
            ).andReturn()
            resultList.add(result)
        }

        // then
        val successCount = resultList.count { it.response.status == HttpStatus.OK.value() }
        val tooManyRequestsCount = resultList.count { it.response.status == HttpStatus.TOO_MANY_REQUESTS.value() }

        assertThat(successCount).isEqualTo(50)
        assertThat(tooManyRequestsCount).isEqualTo(5)
    }

    @DisplayName("동시에 50회 초과 요청 시 50회 - 200, 5회 - 429 가 응답된다.")
    @Test
    fun when50RequestsAtTheSameTimeThen50SucceedAnd5Failed() {
        // given
        `when`(movieUseCase.getAvailableMovies(any(), any())).thenReturn(emptyList())

        val futures = mutableListOf<CompletableFuture<MvcResult>>()

        // when
        repeat(55) {
            val future = CompletableFuture.supplyAsync(
                mockMvc.perform(
                    get("/api/movies")
                        .header("X-Forwarded-For", "127.0.0.1")
                )::andReturn
            )
            futures.add(future)
        }

        val results = futures.map { it.join() }

        // then
        val successCount = results.count { it.response.status == HttpStatus.OK.value() }
        val tooManyRequestsCount = results.count { it.response.status == HttpStatus.TOO_MANY_REQUESTS.value() }

        assertThat(successCount).isEqualTo(50)
        assertThat(tooManyRequestsCount).isEqualTo(5)
    }

}