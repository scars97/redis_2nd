package com.example.infrastructure.out.persistence.repository

import com.example.business.movie.domain.Movie
import com.example.business.movie.repository.MovieRepository
import com.example.infrastructure.out.persistence.mapper.MovieMapper
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
class MovieCoreRepositoryImpl(
    private val jpaRepository: MovieJpaRepository
): MovieRepository {

    override fun getMoviesReleasedUntil(now: LocalDate): List<Movie> {
        val movies = jpaRepository.findByReleaseDateLessThanEqualOrderByReleaseDateAsc(now)

        return movies.stream()
            .map { MovieMapper.toMovie(it) }
            .toList()
    }

}