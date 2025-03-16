package com.example.business.movie.domain

import java.time.LocalDate

data class Movie (
    val movieId: Long,
    val title: String,
    val thumbnail: String,
    val releaseDate: LocalDate,
    val runTime: Int,
    val genre: String,
    val rating: String
)