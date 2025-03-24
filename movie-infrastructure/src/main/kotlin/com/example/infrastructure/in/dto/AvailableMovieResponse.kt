package com.example.infrastructure.`in`.dto

import com.example.application.dto.AvailableMovieResult

data class AvailableMovieResponse(
    val movie: MovieResponse,
    val theaters: List<TheaterResponse> = listOf(),
    val theaterSchedules: List<TheaterScheduleResponse> = listOf()
) {

    companion object {
        fun of(result: AvailableMovieResult): AvailableMovieResponse {
            val movieResponse = MovieResponse.of(result.movie)

            val theaterResponse = result.theaters.stream()
                .map { TheaterResponse.of(it) }
                .toList()

            val scheduleResponse = result.theaterSchedules.stream()
                .map { TheaterScheduleResponse.of(it) }
                .toList()

            return AvailableMovieResponse(
                movie = movieResponse,
                theaters = theaterResponse,
                theaterSchedules = scheduleResponse
            )
        }
    }

}
