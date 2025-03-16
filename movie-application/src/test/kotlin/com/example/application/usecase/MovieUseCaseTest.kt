package com.example.application.usecase

import com.example.application.movie.MovieUseCase
import com.example.business.movie.domain.Movie
import com.example.business.movie.service.MovieService
import com.example.business.theater.domain.Theater
import com.example.business.theater.domain.TheaterSchedule
import com.example.business.theater.service.TheaterScheduleService
import com.example.business.theater.service.TheaterService
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import java.time.LocalDate
import java.time.LocalTime

@ExtendWith(MockitoExtension::class)
class MovieUseCaseTest {

    @InjectMocks
    private lateinit var sut: MovieUseCase

    @Mock
    private lateinit var movieService: MovieService

    @Mock
    private lateinit var theaterService: TheaterService

    @Mock
    private lateinit var scheduleService: TheaterScheduleService

    private lateinit var movie: Movie
    private lateinit var theater: Theater
    private lateinit var schedule1: TheaterSchedule
    private lateinit var schedule2: TheaterSchedule

    @BeforeEach
    fun setUp() {
        sut = MovieUseCase(movieService, theaterService, scheduleService)

        movie = Movie(
            movieId = 1L,
            title = "영화 A",
            thumbnail = "url",
            releaseDate = LocalDate.now().minusDays(1),
            runTime = 120,
            genre = "액션",
            rating = "전체 이용가"
        )
        theater = Theater(theaterId = 1L, name = "A관")
        schedule1 = TheaterSchedule(
            scheduleId = 1L,
            movieId = 1L,
            theaterId = 1L,
            screeningDate = LocalDate.now(),
            startTime = LocalTime.of(12, 0),
            endTime = LocalTime.of(14, 0)
        )
        schedule2 = TheaterSchedule(
            scheduleId = 2L,
            movieId = 1L,
            theaterId = 1L,
            screeningDate = LocalDate.now(),
            startTime = LocalTime.of(14, 0),
            endTime = LocalTime.of(16, 0)
        )
    }

    @DisplayName("현재 상영 중인 영화 목록이 반환된다.")
    @Test
    fun getAvailableMovies() {
        // given
        `when`(movieService.getAvailableMovies()).thenReturn(listOf(movie))
        `when`(scheduleService.getSchedules(listOf(movie))).thenReturn(listOf(schedule1, schedule2))
        `when`(theaterService.getTheaters(setOf(schedule1.theaterId, schedule2.theaterId))).thenReturn(listOf(theater))

        // when
        val result = sut.getAvailableMovies()

        // then
        assertThat(result).hasSize(1)
            .extracting("movieId", "releaseDate")
            .containsExactly(tuple(1L, LocalDate.now().minusDays(1)))
        assertThat(result[0].theaters).hasSize(1)
            .extracting("theaterId", "name")
            .containsExactly(tuple(1L, "A관"))
        assertThat(result[0].theaterSchedules).hasSize(2)
            .extracting("scheduleId", "movieId", "theaterId", "startTime")
            .containsExactly(
                tuple(1L, 1L, 1L, LocalTime.of(12, 0)),
                tuple(2L, 1L, 1L, LocalTime.of(14, 0))
            )
    }

}