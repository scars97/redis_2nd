package com.example.application.usecase

import com.example.application.dto.AvailableMovieResult
import com.example.business.movie.domain.Movie
import com.example.business.movie.service.MovieService
import com.example.business.theater.domain.Theater
import com.example.business.theater.domain.TheaterSchedule
import com.example.business.theater.service.TheaterScheduleService
import com.example.business.theater.service.TheaterService
import org.springframework.stereotype.Component

@Component
class MovieUseCase(
    private val movieService: MovieService,
    private val theaterService: TheaterService,
    private val scheduleService: TheaterScheduleService
){

    fun getAvailableMovies(): List<AvailableMovieResult> {
        // 상영 가능한 영화 목록 조회
        val availableMovies: List<Movie> = movieService.getAvailableMovies()

        // 영화 상영 일정 조회
        val scheduleMap: Map<Long, List<TheaterSchedule>> = scheduleService.getSchedules(availableMovies)
            .groupBy { it.movieId }

        // 영화 상영관 조회
        val theaterIds: Set<Long> = scheduleMap.values.flatten().map { it.theaterId }.toSet()
        val theaterMap: Map<Long, Theater> = theaterService.getTheaters(theaterIds)
            .associateBy { it.theaterId }

        return availableMovies.map { movie ->
            val schedules = scheduleMap[movie.movieId] ?: emptyList()
            val theaters = schedules.mapNotNull { theaterMap[it.theaterId] }.toSet()
            AvailableMovieResult.of(movie, theaters, schedules)
        }.toList()
    }

}