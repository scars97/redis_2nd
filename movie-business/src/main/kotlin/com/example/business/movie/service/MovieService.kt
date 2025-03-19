package com.example.business.movie.service

import com.example.business.movie.domain.Movie
import com.example.business.movie.repository.MovieRepository
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class MovieService(
    private val movieRepository: MovieRepository
) {
    fun getAvailableMovies() : List<Movie> {
        return movieRepository.getMoviesReleasedUntil(LocalDate.now())
    }
}