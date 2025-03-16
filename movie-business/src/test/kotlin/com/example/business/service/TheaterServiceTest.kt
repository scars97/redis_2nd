package com.example.business.service

import com.example.business.theater.domain.Theater
import com.example.business.theater.repository.TheaterRepository
import com.example.business.theater.service.TheaterService
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class TheaterServiceTest {

    @Mock
    private lateinit var theaterRepository: TheaterRepository

    private lateinit var sut: TheaterService

    @BeforeEach
    fun setUp() {
        sut = TheaterService(theaterRepository)
    }

    @DisplayName("특정 상영관 목록이 반환된다.")
    @Test
    fun getTheaters() {
        // given
        val theaterIds = setOf(1L, 2L)
        val theater1 = Theater(theaterId = 1L, name = "A관")
        val theater2 = Theater(theaterId = 2L, name = "B관")

        `when`(theaterRepository.getTheatersBy(theaterIds))
            .thenReturn(listOf(theater1, theater2))

        // when
        val result = sut.getTheaters(theaterIds)

        // then
        assertThat(result).hasSize(2)
            .extracting("theaterId", "name")
            .containsExactly(
                tuple(1L, "A관"),
                tuple(2L, "B관")
            )
    }

}