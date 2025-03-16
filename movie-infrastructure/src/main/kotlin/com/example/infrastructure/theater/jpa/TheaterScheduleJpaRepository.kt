package com.example.infrastructure.theater.jpa

import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository

interface TheaterScheduleJpaRepository: JpaRepository<TheaterScheduleEntity, Long> {

    @EntityGraph(attributePaths = ["theater"])
    fun findByMovieIdInOrderByStartTimeAsc(movieIds: List<Long>): List<TheaterScheduleEntity>

}