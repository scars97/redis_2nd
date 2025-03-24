package com.example.business.movie.service

import com.example.business.movie.domain.Movie
import com.example.business.movie.repository.MovieRepository
import org.springframework.stereotype.Service

@Service
class MovieService(
    private val movieRepository: MovieRepository
) {
    fun getAvailableMovies(title: String?, genre: String?) : List<Movie> {
        return movieRepository.getMoviesReleasedUntil(title, genre)
    }
}