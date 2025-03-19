package com.example.infrastructure.out.persistence.repository

import com.example.infrastructure.out.persistence.entity.MovieEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDate

interface MovieJpaRepository: JpaRepository<MovieEntity, Long> {

    fun findByReleaseDateLessThanEqualOrderByReleaseDateAsc(now: LocalDate): List<MovieEntity>

}