package com.example.application.usecase

import com.example.application.fixture.MovieUseCaseFixture
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

    private val fixture: MovieUseCaseFixture = MovieUseCaseFixture()

    @BeforeEach
    fun setUp() {
        sut = MovieUseCase(movieService, theaterService, scheduleService)
    }

    @DisplayName("현재 상영 중인 영화 목록이 반환된다.")
    @Test
    fun getAvailableMovies() {
        // given
        val movie = fixture.createMovie(1L, "영화 A", LocalDate.now().minusDays(1))
        val theater = fixture.createTheater(1L, "A관")
        val schedule1 = fixture.createSchedule(1L, 1L, 1L, LocalTime.of(12,0), LocalTime.of(14, 0))
        val schedule2 = fixture.createSchedule(2L, 1L, 1L, LocalTime.of(14,0), LocalTime.of(16, 0))

        `when`(movieService.getAvailableMovies()).thenReturn(listOf(movie))
        `when`(scheduleService.getSchedules(listOf(movie))).thenReturn(listOf(schedule1, schedule2))
        `when`(theaterService.getTheaters(setOf(schedule1.theaterId, schedule2.theaterId))).thenReturn(listOf(theater))

        // when
        val result = sut.getAvailableMovies()

        // then
        assertThat(result[0].movie)
            .extracting("movieId", "releaseDate")
            .containsExactly(1L, LocalDate.now().minusDays(1))
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