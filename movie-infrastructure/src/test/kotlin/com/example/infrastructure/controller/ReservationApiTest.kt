package com.example.infrastructure.controller

import com.example.application.dto.ReservationResult
import com.example.application.usecase.ReservationUseCase
import com.example.infrastructure.config.IntegrationTestSupport
import com.example.infrastructure.`in`.dto.CreateReservationRequest
import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*

class ReservationApiTest: IntegrationTestSupport() {

    @MockitoBean
    private lateinit var reservationUseCase: ReservationUseCase

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    data class TestCase(
        val description: String,
        val request: CreateReservationRequest,
        val expectedResult: ReservationResult,
        val expectStatus: HttpStatus
    )

    @DisplayName("같은 시간대의 영화를 5분에 1번씩 예약할 수 있으며, 요청 초과 시 429 Too Many Requests 를 응답 받는다.")
    @TestFactory
    fun whenExceedRequestThenReturn429TooManyRequests(): List<DynamicTest> {
        val testCases = listOf(
            TestCase(
                description = "1번 요청은 201 Created 를 응답 받는다.",
                request = CreateReservationRequest(1L, 1L, listOf(1L, 2L)),
                expectedResult = ReservationResult(1L, 1L, listOf(1L, 2L)),
                expectStatus = HttpStatus.CREATED
            ),
            TestCase(
                description = "2번 요청은 429 Too Many Requests 를 응답 받는다.",
                request = CreateReservationRequest(1L, 1L, listOf(3L, 4L)),
                expectedResult = ReservationResult(1L, 1L, listOf(3L, 4L)),
                expectStatus = HttpStatus.TOO_MANY_REQUESTS
            )
        )

        return resultOf(testCases)
    }

    @DisplayName("다른 시간대의 영화는 대기 시간 없이 예약할 수 있다.")
    @TestFactory
    fun movieInOtherTimeWithNoWaitTime(): List<DynamicTest> {
        val testCases = listOf(
            TestCase(
                description = "1번 일정으로 요청 시, 201 Created 를 응답 받는다.",
                request = CreateReservationRequest(1L, 1L, listOf(1L, 2L)),
                expectedResult = ReservationResult(1L, 1L, listOf(1L, 2L)),
                expectStatus = HttpStatus.CREATED
            ),
            TestCase(
                description = "2번 일정으로 요청 시, 201 Created 를 응답 받는다.",
                request = CreateReservationRequest(1L, 2L, listOf(1L, 2L)),
                expectedResult = ReservationResult(1L, 2L, listOf(1L, 2L)),
                expectStatus = HttpStatus.CREATED
            )
        )

        return resultOf(testCases)
    }

    private fun resultOf(testCases: List<TestCase>) =
        testCases.map { testCase ->
            DynamicTest.dynamicTest(testCase.description) {
                // given
                `when`(reservationUseCase.createReservation(testCase.request.toInfo()))
                    .thenReturn(testCase.expectedResult)

                // when
                val result = mockMvc.perform(
                    post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testCase.request))
                ).andReturn()

                // then
                assertThat(result.response.status).isEqualTo(testCase.expectStatus.value())
            }
        }

}