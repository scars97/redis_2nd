package com.example.infrastructure.movie

import com.example.business.movie.domain.Movie
import com.example.business.movie.repository.MovieRepository
import com.example.infrastructure.common.mapper.MovieMapper
import com.example.infrastructure.movie.jpa.MovieJpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
class MovieCoreRepositoryImpl(
    private val jpaRepository: MovieJpaRepository
): MovieRepository {

    override fun findMoviesReleasedUntil(now: LocalDate): List<Movie> {
        val movies = jpaRepository.findByReleaseDateLessThanEqualOrderByReleaseDateAsc(now)

        return movies.stream()
            .map { MovieMapper.toMovie(it) }
            .toList()
    }

}