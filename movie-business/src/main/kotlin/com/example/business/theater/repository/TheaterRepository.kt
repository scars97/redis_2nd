package com.example.business.theater.repository

import com.example.business.theater.domain.Theater

interface TheaterRepository {

    fun getTheatersBy(theaterIds: Set<Long>): List<Theater>

}