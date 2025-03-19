package com.example.business.theater.service

import com.example.business.movie.domain.Movie
import com.example.business.theater.domain.TheaterSchedule
import com.example.business.theater.repository.TheaterScheduleRepository
import org.springframework.stereotype.Service

@Service
class TheaterScheduleService (
    private val scheduleRepository: TheaterScheduleRepository
){

    fun getSchedules(movies: List<Movie>): List<TheaterSchedule> {
        val movieIds: List<Long> = movies.map { it.movieId }

        return scheduleRepository.getScheduleBy(movieIds)
    }

}