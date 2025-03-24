package com.example.infrastructure.`in`.dto

import com.example.application.dto.MovieResult
import com.example.business.movie.domain.Movie
import java.time.LocalDate

data class MovieResponse (
    val movieId: Long,
    val title: String,
    val releaseDate: LocalDate,
    val thumbnail: String,
    val runTime: Int,
    val genre: String,
    val rating: String
) {

    companion object {
        fun of(movie: MovieResult): MovieResponse {
            return MovieResponse(
                movieId = movie.movieId,
                title = movie.title,
                releaseDate = movie.releaseDate,
                thumbnail = movie.thumbnail,
                runTime = movie.runTime,
                genre = movie.genre,
                rating = movie.rating
            )
        }
    }

}