package com.example.infrastructure.out.persistence.repository

import com.example.business.theater.domain.TheaterSchedule
import com.example.business.theater.repository.TheaterScheduleRepository
import com.example.infrastructure.out.persistence.mapper.TheaterScheduleMapper
import org.springframework.stereotype.Repository

@Repository
class TheaterScheduleCoreRepositoryImpl(
    private val jpaRepository: TheaterScheduleJpaRepository
): TheaterScheduleRepository {

    override fun getScheduleBy(movieIds: List<Long>): List<TheaterSchedule> {
        val schedules = jpaRepository.findByMovieIdInOrderByStartTimeAsc(movieIds)

        return schedules.stream()
            .map { TheaterScheduleMapper.toSchedule(it) }
            .toList()
    }

}