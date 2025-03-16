package com.example.business.theater.service

import com.example.business.theater.domain.Theater
import com.example.business.theater.repository.TheaterRepository
import org.springframework.stereotype.Service

@Service
class TheaterService(
    private val theaterRepository: TheaterRepository
) {

    fun getTheaters(theaterIds: Set<Long>): List<Theater> {
        return theaterRepository.getTheatersBy(theaterIds)
    }

}