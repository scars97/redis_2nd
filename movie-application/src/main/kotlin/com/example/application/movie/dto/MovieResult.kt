package com.example.application.movie.dto

import com.example.business.movie.domain.Movie
import com.example.business.theater.domain.Theater
import com.example.business.theater.domain.TheaterSchedule
import java.time.LocalDate

data class MovieResult(
    val movieId: Long,
    val title: String,
    val releaseDate: LocalDate,
    val thumbnail: String,
    val runTime: Int,
    val genre: String,
    val rating: String,
    val theaters: Set<Theater> = setOf(),
    val theaterSchedules: List<TheaterSchedule> = listOf()
) {
    companion object {
        fun of(movie: Movie, theaters: Set<Theater>, schedules: List<TheaterSchedule>): MovieResult {
            return MovieResult(
                movieId = movie.movieId,
                title = movie.title,
                releaseDate = movie.releaseDate,
                thumbnail = movie.thumbnail,
                runTime = movie.runTime,
                genre = movie.genre,
                rating = movie.rating,
                theaters = theaters,
                theaterSchedules = schedules
            )
        }
    }
}
