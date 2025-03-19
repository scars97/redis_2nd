package com.example.application.dto

import com.example.business.movie.domain.Movie
import java.time.LocalDate

data class MovieResult(
    val movieId: Long,
    val title: String,
    val releaseDate: LocalDate,
    val thumbnail: String,
    val runTime: Int,
    val genre: String,
    val rating: String
) {

    companion object {
        fun of(movie: Movie): MovieResult {
            return MovieResult(
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
