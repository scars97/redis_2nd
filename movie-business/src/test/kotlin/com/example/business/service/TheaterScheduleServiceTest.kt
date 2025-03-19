package com.example.business.service

import com.example.business.movie.domain.Movie
import com.example.business.theater.domain.TheaterSchedule
import com.example.business.theater.repository.TheaterScheduleRepository
import com.example.business.theater.service.TheaterScheduleService
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import java.time.LocalDate
import java.time.LocalTime

@ExtendWith(MockitoExtension::class)
class TheaterScheduleServiceTest {

    @Mock
    private lateinit var scheduleRepository: TheaterScheduleRepository

    private lateinit var sut: TheaterScheduleService

    private lateinit var movie: Movie

    @BeforeEach
    fun setUp() {
        sut = TheaterScheduleService(scheduleRepository)

        movie = Movie(
            movieId = 1L,
            title = "영화 A",
            thumbnail = "url",
            releaseDate = LocalDate.now().minusDays(1),
            runTime = 120,
            genre = "액션",
            rating = "전체 이용가"
        )
    }

    @DisplayName("영화 ID에 해당하는 상영 일정 목록이 반환된다.")
    @Test
    fun getSchedules() {
        // given
        val schedule = TheaterSchedule(
            scheduleId = 1L,
            movieId = 1L,
            theaterId = 1L,
            screeningDate = LocalDate.now(),
            startTime = LocalTime.of(12, 0),
            endTime = LocalTime.of(14, 0)
        )

        `when`(scheduleRepository.getScheduleBy(listOf(movie.movieId)))
            .thenReturn(listOf(schedule))

        // when
        val result = sut.getSchedules(listOf(movie))

        // then
        assertThat(result).hasSize(1)
            .extracting("scheduleId", "movieId", "screeningDate", "startTime")
            .containsExactly(
                tuple(1L, 1L, LocalDate.now(), LocalTime.of(12, 0))
            )
    }
}