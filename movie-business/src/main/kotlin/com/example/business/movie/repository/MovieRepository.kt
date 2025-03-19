package com.example.business.movie.repository

import com.example.business.movie.domain.Movie
import java.time.LocalDate

interface MovieRepository {

    fun getMoviesReleasedUntil(now: LocalDate): List<Movie>

}