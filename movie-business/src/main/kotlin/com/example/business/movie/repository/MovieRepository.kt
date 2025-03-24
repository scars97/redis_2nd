package com.example.business.movie.repository

import com.example.business.movie.domain.Movie

interface MovieRepository {

    fun getMoviesReleasedUntil(title: String?, genre: String?): List<Movie>

}