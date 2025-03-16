package com.example.business.theater.repository

import com.example.business.theater.domain.TheaterSchedule

interface TheaterScheduleRepository {

    fun getScheduleByMovieIds(movieIds: List<Long>): List<TheaterSchedule>

}