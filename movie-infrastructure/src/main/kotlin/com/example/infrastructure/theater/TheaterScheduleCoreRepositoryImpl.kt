package com.example.infrastructure.theater

import com.example.business.theater.domain.TheaterSchedule
import com.example.business.theater.repository.TheaterScheduleRepository
import com.example.infrastructure.common.mapper.TheaterMapper
import com.example.infrastructure.theater.jpa.TheaterScheduleJpaRepository
import org.springframework.stereotype.Repository

@Repository
class TheaterScheduleCoreRepositoryImpl(
    private val jpaRepository: TheaterScheduleJpaRepository
): TheaterScheduleRepository {

    override fun getScheduleByMovieIds(movieIds: List<Long>): List<TheaterSchedule> {
        val schedules = jpaRepository.findByMovieIdInOrderByStartTimeAsc(movieIds)

        return schedules.stream()
            .map { TheaterMapper.toSchedule(it) }
            .toList()
    }

}