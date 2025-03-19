package com.example.infrastructure.out.persistence.repository

import com.example.business.theater.domain.Theater
import com.example.business.theater.repository.TheaterRepository
import com.example.infrastructure.out.persistence.mapper.TheaterMapper
import org.springframework.stereotype.Repository

@Repository
class TheaterCoreRepositoryImpl(
    private val jpaRepository: TheaterJpaRepository
): TheaterRepository {

    override fun getTheatersBy(theaterIds: Set<Long>): List<Theater> {
        val theaters = jpaRepository.findByIdIn(theaterIds)

        return theaters.stream()
            .map { TheaterMapper.toTheater(it) }
            .toList()
    }

}