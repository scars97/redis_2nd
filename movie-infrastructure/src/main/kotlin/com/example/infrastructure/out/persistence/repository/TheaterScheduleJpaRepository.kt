package com.example.infrastructure.out.persistence.repository

import com.example.infrastructure.out.persistence.entity.TheaterScheduleEntity
import org.springframework.data.jpa.repository.JpaRepository

interface TheaterScheduleJpaRepository: JpaRepository<TheaterScheduleEntity, Long> {

    fun findByMovieIdInOrderByStartTimeAsc(movieIds: List<Long>): List<TheaterScheduleEntity>

}