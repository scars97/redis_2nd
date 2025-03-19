package com.example.business.service

import com.example.business.movie.domain.Movie
import com.example.business.movie.repository.MovieRepository
import com.example.business.movie.service.MovieService
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import java.time.LocalDate
import kotlin.test.Test

@ExtendWith(MockitoExtension::class)
class MovieServiceTest {

    @Mock
    private lateinit var movieRepository: MovieRepository
    private lateinit var sut: MovieService

    @BeforeEach
    fun setUp() {
        sut = MovieService(movieRepository)
    }

    @DisplayName("현재 날짜를 기준으로 개봉일이 지난 영화 목록을 조회한다.")
    @Test
    fun getAvailableMovies() {
        // given
        val now = LocalDate.now()
        val movie = Movie(
            movieId = 1L,
            title = "영화 A",
            thumbnail = "url",
            releaseDate = LocalDate.now().minusDays(1),
            runTime = 120,
            genre = "액션",
            rating = "전체 이용가"
        )

        `when`(movieRepository.getMoviesReleasedUntil(now))
            .thenReturn(listOf(movie))

        // when
        val result = sut.getAvailableMovies()

        // then
        assertThat(result).hasSize(1)
            .extracting("movieId", "title", "releaseDate")
            .containsExactly(
                tuple(1L, "영화 A", now.minusDays(1))
            )
    }
}