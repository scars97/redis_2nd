package com.example.application.dto

import com.example.business.movie.domain.Movie
import com.example.business.theater.domain.Theater
import com.example.business.theater.domain.TheaterSchedule

data class AvailableMovieResult(
    val movie: MovieResult,
    val theaters: List<TheaterResult> = listOf(),
    val theaterSchedules: List<TheaterScheduleResult> = listOf()
) {

    companion object {
        fun of(movie: Movie, theaters: Set<Theater>, schedules: List<TheaterSchedule>): AvailableMovieResult {
            val movieResult = MovieResult.of(movie)

            val theaterResult = theaters.stream()
                .map { TheaterResult.of(it) }
                .toList()

            val scheduleResult = schedules.stream()
                .map { TheaterScheduleResult.of(it) }
                .toList()

            return AvailableMovieResult(
                movie = movieResult,
                theaters = theaterResult,
                theaterSchedules = scheduleResult
            )
        }
    }

}
